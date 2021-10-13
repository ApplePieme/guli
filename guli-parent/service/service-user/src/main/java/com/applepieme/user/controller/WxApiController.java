package com.applepieme.user.controller;

import com.applepieme.base.exception.GuliException;
import com.applepieme.user.entity.Member;
import com.applepieme.user.service.MemberService;
import com.applepieme.user.util.ConstantPropertiesUtils;
import com.applepieme.user.util.HttpClientUtils;
import com.applepieme.util.JwtUtils;
import com.applepieme.util.ResultCode;
import com.google.gson.Gson;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author applepieme
 * @date 2021/10/6 20:03
 */
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private MemberService memberService;

    @GetMapping("/callback")
    public String callback(String code, String state, HttpSession session) {
        /* 从redis中将state获取出来，和当前传入的state作比较
           如果一致则放行，如果不一致则抛出异常：非法访问 */
        String originState = redisTemplate.opsForValue().get(session.getId());
        if (StringUtils.isEmpty(originState)) {
            throw new GuliException(ResultCode.ERROR, "登录凭证已过期，请重新登录");
        }
        if (!originState.equals(state)) {
            throw new GuliException(ResultCode.ERROR, "非法访问");
        }

        /* 向认证服务器发送请求换取access_token */
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String accessTokenUrl = String.format(baseAccessTokenUrl, ConstantPropertiesUtils.WX_OPEN_APP_ID,
                ConstantPropertiesUtils.WX_OPEN_APP_SECRET, code);
        String result;
        try {
            /* 调用 httpclient 发送请求 */
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCode.ERROR, "获取用户信息失败");
        }
        /* 解析返回的json字符串 */
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String) map.get("access_token");
        String openid = (String) map.get("openid");

        /* 根据 openid 查询用户是否已经注册，未注册就获取用户信息进行注册 */
        Member member = memberService.getMemberByOpenid(openid);
        if (member == null) {
            /* 拿到 access_token 和 openid 再去请求微信服务器获取用户信息 */
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String userInfoJson;
            try {
                userInfoJson = HttpClientUtils.get(userInfoUrl);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GuliException(ResultCode.ERROR, "获取用户信息失败");
            }

            /* 解析返回的 json 字符串 */
            HashMap userInfo = gson.fromJson(userInfoJson, HashMap.class);
            String nickname = (String) userInfo.get("nickname");
            String avatar = (String) userInfo.get("headimgurl");

            /* 向数据库中插入新用户记录 */
            member = new Member();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(avatar);
            memberService.save(member);
        }

        /* 生成 token 通过参数返回 */
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return "redirect:http://localhost:3000?token=" + token;
    }

    @GetMapping("/login")
    public String getQrConnect(HttpSession session) {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        // 获取业务服务器重定向地址
        String redirectUrl = ConstantPropertiesUtils.WX_OPEN_REDIRECT_URL;
        try {
            // url编码
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new GuliException(ResultCode.ERROR, "获取微信登录二维码失败");
        }

        /* 防止csrf攻击（跨站请求伪造攻击）
         *  生成一个随机唯一的 state 这个参数在用户扫码后会返回
         *  把 state 存放到 Redis 中 过期时间设置 30 分钟
         *  在 callback 方法中进行对比 */
        String state = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(session.getId(), state, 10, TimeUnit.MINUTES);

        String qrConnectUrl = String.format(baseUrl, ConstantPropertiesUtils.WX_OPEN_APP_ID, redirectUrl, state);
        return "redirect:" + qrConnectUrl;
    }
}

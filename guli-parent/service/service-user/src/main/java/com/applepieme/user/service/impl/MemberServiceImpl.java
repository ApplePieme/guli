package com.applepieme.user.service.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.user.entity.Member;
import com.applepieme.user.entity.vo.LoginVo;
import com.applepieme.user.entity.vo.RegisterVo;
import com.applepieme.user.mapper.MemberMapper;
import com.applepieme.user.service.MemberService;
import com.applepieme.util.JwtUtils;
import com.applepieme.util.MD5;
import com.applepieme.util.ResultCode;
import com.applepieme.vo.MemberVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-06
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Integer getRegisterNumByDay(String day) {
        return baseMapper.getRegisterNumByDay(day);
    }

    @Override
    public MemberVo getMemberVoById(String id) {
        Member member = baseMapper.selectById(id);
        MemberVo memberVo = new MemberVo();
        BeanUtils.copyProperties(member, memberVo);
        return memberVo;
    }

    @Override
    public String userLogin(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(ResultCode.ERROR, "手机号或密码为空");
        }

        Member member = baseMapper.selectOne(new QueryWrapper<Member>().eq("mobile", mobile));
        if (member == null) {
            throw new GuliException(ResultCode.ERROR, "该手机号未注册");
        }
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(ResultCode.ERROR, "密码错误");
        }
        if (member.getIsDisabled()) {
            throw new GuliException(ResultCode.ERROR, "该用户已被禁止登录");
        }

        return JwtUtils.getJwtToken(member.getId(), member.getNickname());
    }

    @Override
    public void userRegister(RegisterVo registerVo) {
        /* 获取表单数据 */
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();

        /* 判空 */
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)
                || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(ResultCode.ERROR, "必填项不能为空");
        }

        /* 校验验证码 */
        String originCode = redisTemplate.opsForValue().get(mobile);
        if (StringUtils.isEmpty(originCode)) {
            throw new GuliException(ResultCode.ERROR, "验证码已过期");
        }
        if (!code.equals(originCode)) {
            throw new GuliException(ResultCode.ERROR, "验证码错误");
        }

        /* 校验手机号是否已经注册 */
        if (baseMapper.selectCount(new QueryWrapper<Member>().eq("mobile", mobile)) > 0) {
            throw new GuliException(ResultCode.ERROR, "该手机号已注册");
        }

        /* 把用户信息添加到数据库，注册成功 */
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("https://guli-file-applepieme.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        baseMapper.insert(member);
    }

    @Override
    public Member getMemberByOpenid(String openid) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return baseMapper.selectOne(queryWrapper);
    }
}

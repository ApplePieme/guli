package com.applepieme.msm.service.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.msm.service.MsmService;
import com.applepieme.msm.util.ConstantPropertiesUtils;
import com.applepieme.msm.util.RandomUtils;
import com.applepieme.util.ResultCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author applepieme
 * @date 2021/10/6 10:51
 */
@Service
public class MsmServiceImpl implements MsmService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean sendCode(String phone) {
        /* 校验手机号 */
        if (StringUtils.isEmpty(phone) || !Pattern.matches(ConstantPropertiesUtils.TEST_MOBILE, phone)) {
            throw new GuliException(ResultCode.ERROR, "请输入正确的手机号");
        }
        /* 要发送的验证码 */
        String code = RandomUtils.getSixBitRandom();
        /* 拼接请求链接 */
        String sendUrl =
                ConstantPropertiesUtils.HOST + ConstantPropertiesUtils.PATH + "?code=" + code + "&phone=" + phone +
                        "&sign=" + ConstantPropertiesUtils.SIGN + "&skin=" + ConstantPropertiesUtils.SKIN;
        try {
            URL url = new URL(sendUrl);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            /* 格式Authorization:APPCODE (中间是英文空格) */
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + ConstantPropertiesUtils.APPCODE);
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                redisTemplate.opsForValue().set(phone, code, 10, TimeUnit.MINUTES);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCode.ERROR, "验证码发送异常");
        }
    }
}

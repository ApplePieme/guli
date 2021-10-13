package com.applepieme.statistics.client.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.statistics.client.UserClient;
import com.applepieme.util.ResultCode;
import org.springframework.stereotype.Component;

/**
 * @author applepieme
 * @date 2021/10/10 19:17
 */
@Component
public class UserClientImpl implements UserClient {
    @Override
    public Integer getRegisterNumByDay(String day) {
        throw new GuliException(ResultCode.ERROR, "用户服务异常，请稍后重试");
    }
}

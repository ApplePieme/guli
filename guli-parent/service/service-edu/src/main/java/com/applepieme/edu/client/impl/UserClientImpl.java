package com.applepieme.edu.client.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.edu.client.UserClient;
import com.applepieme.util.ResultCode;
import com.applepieme.vo.MemberVo;
import org.springframework.stereotype.Component;

/**
 * @author applepieme
 * @date 2021/10/8 16:15
 */
@Component
public class UserClientImpl implements UserClient {
    @Override
    public MemberVo getMemberVoById(String id) {
        throw new GuliException(ResultCode.ERROR, "服务器异常，请稍后重试");
    }
}

package com.applepieme.order.client.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.order.client.MemberClient;
import com.applepieme.util.ResultCode;
import com.applepieme.vo.MemberVo;
import org.springframework.stereotype.Component;

/**
 * @author applepieme
 * @date 2021/10/8 21:57
 */
@Component
public class MemberClientImpl implements MemberClient {
    @Override
    public MemberVo getMemberVoById(String id) {
        throw new GuliException(ResultCode.ERROR, "用户服务异常，请稍后重试");
    }
}

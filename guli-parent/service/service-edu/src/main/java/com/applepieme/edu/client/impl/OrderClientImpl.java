package com.applepieme.edu.client.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.edu.client.OrderClient;
import com.applepieme.util.ResultCode;
import org.springframework.stereotype.Component;

/**
 * @author applepieme
 * @date 2021/10/9 21:47
 */
@Component
public class OrderClientImpl implements OrderClient {
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        throw new GuliException(ResultCode.ERROR, "订单服务异常，请稍后重试");
    }
}

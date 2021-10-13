package com.applepieme.util;

/**
 * @author applepieme
 * @date 2021/9/29 16:41
 */
public interface ResultCode {
    /**
     * 成功
     */
    Integer SUCCESS = 20000;

    /**
     * 失败
     */
    Integer ERROR = 20001;

    /**
     * 未支付
     */
    Integer NOTPAY = 20002;
}

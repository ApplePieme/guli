package com.applepieme.msm.service;

/**
 * @author applepieme
 * @date 2021/10/6 10:51
 */
public interface MsmService {
    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return 是否发送成功
     */
    boolean sendCode(String phone);
}

package com.applepieme.order.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author applepieme
 * @date 2021/10/9 17:45
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    @Value("${wxpay.appid}")
    private String appid;

    @Value("${wxpay.partner}")
    private String partner;

    @Value("${wxpay.partnerkey}")
    private String partnerkey;

    @Value("${wxpay.notifyurl}")
    private String notifyUrl;

    public static String APPID;
    public static String PARTNER;
    public static String PARTNER_KEY;
    public static String NOTIFY_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        APPID = appid;
        PARTNER = partner;
        PARTNER_KEY = partnerkey;
        NOTIFY_URL = notifyUrl;
    }
}

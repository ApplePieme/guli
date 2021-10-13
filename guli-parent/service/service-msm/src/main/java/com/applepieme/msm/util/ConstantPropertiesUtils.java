package com.applepieme.msm.util;

/**
 * @author applepieme
 * @date 2021/10/6 11:02
 */
public interface ConstantPropertiesUtils {
    // 【1】请求地址 支持http 和 https 及 WEBSOCKET
    String HOST = "https://fesms.market.alicloudapi.com";

    // 【2】后缀
    String PATH = "/sms/";

    // 【3】开通服务后 买家中心-查看AppCode
    String APPCODE = "d199304b7e8346f4b797aa4feb85aabe";

    // 【4】请求参数，签名编号【联系客服人员申请，测试请用1】
    String SIGN = "1";

    // 【4】请求参数，模板编号【联系旺旺客服申请，测试请用1~21】
    String SKIN = "17";

    // 正则表达式用于校验手机号
    String TEST_MOBILE = "^1[34578]\\d{9}$";
}

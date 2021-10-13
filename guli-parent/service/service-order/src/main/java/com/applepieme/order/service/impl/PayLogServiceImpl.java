package com.applepieme.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.applepieme.base.exception.GuliException;
import com.applepieme.order.client.CourseClient;
import com.applepieme.order.entity.Order;
import com.applepieme.order.entity.PayLog;
import com.applepieme.order.mapper.PayLogMapper;
import com.applepieme.order.service.OrderService;
import com.applepieme.order.service.PayLogService;
import com.applepieme.order.util.ConstantPropertiesUtils;
import com.applepieme.order.util.HttpClientUtils;
import com.applepieme.util.ResultCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {
    @Resource
    private OrderService orderService;

    @Resource
    private CourseClient courseClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean updateOrderStatus(Map<String, String> map) {
        /* 获取订单号 */
        String orderNo = map.get("out_trade_no");
        /* 获取订单对象 */
        Order order = orderService.getOrderByOrderNo(orderNo);
        if (order.getStatus() == 1) {
            return true;
        }
        /* 更新订单状态 */
        order.setStatus(1);
        boolean orderUpdated = orderService.updateById(order);
        /* 支付成功后，该课程的销量增加 */
        if (orderUpdated) {
            courseClient.updateCourseBuyCount(order.getCourseId());
        }
        boolean payLogInserted = false;
        if (orderUpdated) {
            /* 向支付日志表中新增一条记录 */
            PayLog payLog = new PayLog();
            payLog.setOrderNo(order.getOrderNo());//支付订单号
            payLog.setPayTime(new Date()); // 支付时间
            payLog.setPayType(1);//支付类型 1微信支付
            payLog.setTotalFee(order.getTotalFee());//总金额(分)
            payLog.setTradeState(map.get("trade_state"));//支付状态
            payLog.setTransactionId(map.get("transaction_id")); // 支付流水号
            payLog.setAttr(JSONObject.toJSONString(map));// 其他属性
            payLogInserted = baseMapper.insert(payLog) > 0;
        }
        return orderUpdated && payLogInserted;
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        /* 封装微信支付参数 */
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", ConstantPropertiesUtils.APPID);
        requestMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
        requestMap.put("out_trade_no", orderNo);
        requestMap.put("nonce_str", WXPayUtil.generateNonceStr());

        /* 封装请求参数 */
        HttpClientUtils httpClient = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/orderquery");
        try {
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(requestMap, ConstantPropertiesUtils.PARTNER_KEY));
            httpClient.setHttps(true);
            httpClient.post();

            /* 接收微信返回的数据 */
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCode.ERROR, "查询订单状态失败");
        }
    }

    @Override
    public Map<String, Object> createNative(String orderNo) {
        /* 根据订单号获取订单信息 */
        Order order = orderService.getOrderByOrderNo(orderNo);

        /* 封装微信支付请求参数 */
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", ConstantPropertiesUtils.APPID);
        requestMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
        requestMap.put("nonce_str", WXPayUtil.generateNonceStr());
        requestMap.put("body", order.getCourseTitle());
        requestMap.put("out_trade_no", orderNo);
        requestMap.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
        requestMap.put("spbill_create_ip", "127.0.0.1");
        requestMap.put("notify_url", ConstantPropertiesUtils.NOTIFY_URL);
        requestMap.put("trade_type", "NATIVE");

        /* 设置请求参数 */
        HttpClientUtils httpClient = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");
        try {
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(requestMap, ConstantPropertiesUtils.PARTNER_KEY));
            httpClient.setHttps(true);
            httpClient.post();

            /* 接收响应内容 */
            String content = httpClient.getContent();
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);

            /* 封装返回的结果 */
            Map<String, Object> result = new HashMap<>();
            result.put("orderNo", orderNo);
            result.put("courseId", order.getCourseId());
            result.put("totalFee", order.getTotalFee());
            result.put("resultCode", responseMap.get("result_code"));
            result.put("codeUrl", responseMap.get("code_url"));

            /* 微信支付二维码2小时过期，可采取2小时未支付取消订单 */
            //redisTemplate.opsForValue().set(orderNo, result, 120, TimeUnit.MINUTES);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCode.ERROR, "获取支付二维码失败");
        }
    }
}

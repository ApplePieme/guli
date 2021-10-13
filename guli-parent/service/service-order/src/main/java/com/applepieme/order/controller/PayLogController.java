package com.applepieme.order.controller;


import com.applepieme.order.service.PayLogService;
import com.applepieme.util.R;
import com.applepieme.util.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
@RestController
@RequestMapping("/order/pay_log")
public class PayLogController {
    @Resource
    private PayLogService payLogService;

    @ApiOperation(value = "查询支付状态")
    @GetMapping("/pay_status/{orderNo}")
    public R queryPayStatus(@PathVariable("orderNo") String orderNo) {
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        if ("SUCCESS".equals(map.get("trade_state"))) {
            if (payLogService.updateOrderStatus(map)) {
                return R.ok().message("支付成功");
            }
            return R.error().message("支付失败");
        }
        return R.ok().code(ResultCode.NOTPAY).message("支付中");
    }

    @ApiOperation(value = "生成支付二维码")
    @GetMapping("/create_native/{orderNo}")
    public R createNative(@PathVariable("orderNo") String orderNo) {
        Map<String, Object> map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }
}


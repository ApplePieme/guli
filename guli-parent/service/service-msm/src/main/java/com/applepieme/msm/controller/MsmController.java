package com.applepieme.msm.controller;

import com.applepieme.msm.service.MsmService;
import com.applepieme.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author applepieme
 * @date 2021/10/6 10:50
 */
@RestController
@RequestMapping("/api/msm")
public class MsmController {
    @Resource
    private MsmService msmService;

    @GetMapping("/{phone}")
    public R sendCode(@PathVariable("phone") String phone) {
        if (msmService.sendCode(phone)) {
            return R.ok().message("验证码发送成功");
        }
        return R.error().message("验证码发送失败");
    }
}

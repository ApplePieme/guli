package com.applepieme.base.handler;

import com.applepieme.base.exception.GuliException;
import com.applepieme.util.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @author applepieme
 * @date 2021/9/29 19:18
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("服务器出错啦！请稍后重试！");
    }

    @ResponseBody
    @ExceptionHandler(GuliException.class)
    public R error(GuliException e) {
        e.printStackTrace();
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}

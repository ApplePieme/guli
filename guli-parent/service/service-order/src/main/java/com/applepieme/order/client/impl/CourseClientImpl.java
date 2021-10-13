package com.applepieme.order.client.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.order.client.CourseClient;
import com.applepieme.util.ResultCode;
import com.applepieme.vo.CourseDetailsVo;
import org.springframework.stereotype.Component;

/**
 * @author applepieme
 * @date 2021/10/8 21:57
 */
@Component
public class CourseClientImpl implements CourseClient {
    @Override
    public void updateCourseBuyCount(String courseId) {
        throw new GuliException(ResultCode.ERROR, "课程服务异常，请稍后重试");
    }

    @Override
    public CourseDetailsVo orderGetCourseDetailsVoById(String id) {
        throw new GuliException(ResultCode.ERROR, "课程服务异常，请稍后重试");
    }
}

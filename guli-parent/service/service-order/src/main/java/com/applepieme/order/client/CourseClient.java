package com.applepieme.order.client;

import com.applepieme.order.client.impl.CourseClientImpl;
import com.applepieme.vo.CourseDetailsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author applepieme
 * @date 2021/10/8 21:57
 */
@Component
@FeignClient(name = "service-edu", fallback = CourseClientImpl.class)
public interface CourseClient {
    /**
     * 更新课程的销量
     *
     * @param courseId 课程 id
     */
    @GetMapping("/edu/course/update_buy_count/{courseId}")
    void updateCourseBuyCount(@PathVariable("courseId") String courseId);

    /**
     * 根据课程id获取课程信息
     *
     * @param id 课程id
     * @return 课程信息对象
     */
    @GetMapping("/edu/course/order/{id}")
    CourseDetailsVo orderGetCourseDetailsVoById(@PathVariable("id") String id);
}

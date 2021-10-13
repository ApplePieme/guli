package com.applepieme.edu.controller;

import com.applepieme.edu.entity.Course;
import com.applepieme.edu.entity.Teacher;
import com.applepieme.edu.service.CourseService;
import com.applepieme.edu.service.TeacherService;
import com.applepieme.util.R;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author applepieme
 * @date 2021/10/5 17:50
 */
@RestController
@RequestMapping("/edu/index")
public class IndexController {
    @Resource
    private CourseService courseService;

    @Resource
    private TeacherService teacherService;

    @GetMapping("")
    public R index() {
        /*查询前 8 条热门课程*/
        List<Course> courseList = courseService.getHotCourseList();

        /*查询前 4 个热门讲师*/
        List<Teacher> teacherList = teacherService.getHotTeacherList();

        return R.ok().data("courseList", courseList).data("teacherList", teacherList);
    }
}

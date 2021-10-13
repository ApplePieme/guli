package com.applepieme.edu.controller;


import com.applepieme.edu.client.OrderClient;
import com.applepieme.edu.entity.Course;
import com.applepieme.edu.entity.query.CourseQuery;
import com.applepieme.edu.entity.query.CourseQueryFront;
import com.applepieme.edu.entity.vo.ChapterVo;
import com.applepieme.util.JwtUtils;
import com.applepieme.vo.CourseDetailsVo;
import com.applepieme.edu.entity.vo.CourseInfoVo;
import com.applepieme.edu.entity.vo.CoursePublishVo;
import com.applepieme.edu.service.ChapterService;
import com.applepieme.edu.service.CourseService;
import com.applepieme.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
@RestController
@RequestMapping("/edu/course")
public class CourseController {
    @Resource
    private CourseService courseService;

    @Resource
    private ChapterService chapterService;

    @Resource
    private OrderClient orderClient;

    @GetMapping("/update_buy_count/{courseId}")
    public void updateCourseBuyCount(@PathVariable("courseId") String courseId) {
        courseService.updateCourseBuyCount(courseId);
    }

    @ApiOperation(value = "订单模块根据id获取课程信息")
    @GetMapping("/order/{id}")
    public CourseDetailsVo orderGetCourseDetailsVoById(@PathVariable("id") String id) {
        return courseService.getCourseDetailsVoById(id);
    }

    @ApiOperation(value = "前台根据id查看课程详情")
    @GetMapping("/details/{id}")
    public R getCourseDetailsById(@PathVariable("id") String id, HttpServletRequest request) {
        courseService.updateCourseViewCount(id);
        CourseDetailsVo courseDetailsVo = courseService.getCourseDetailsVoById(id);
        List<ChapterVo> chapterVoList = chapterService.nestedList(id);

        /* 判断用户是否购买该课程 */
        boolean isBuy = false;
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (!StringUtils.isEmpty(memberId)) {
            isBuy = orderClient.isBuyCourse(id, memberId);
        }
        return R.ok().data("course", courseDetailsVo).data("chapterList", chapterVoList).data("isBuy", isBuy);
    }

    @ApiOperation(value = "前台条件组合分页查询课程列表")
    @PostMapping("/front/{current}/{limit}")
    public R listPageCoursesForFront(@PathVariable("current") Long current,
                                     @PathVariable("limit") Long limit,
                                     @RequestBody CourseQueryFront courseQueryFront) {
        Map<String, Object> map = courseService.listPageCoursesForFront(new Page<>(current, limit), courseQueryFront);
        return R.ok().data(map);
    }

    @ApiOperation(value = "新增课程")
    @PostMapping("/save")
    public R saveCourseInfo(@ApiParam(name = "courseInfoVo", value = "课程基本信息", required = true)
                                @RequestBody CourseInfoVo courseInfoVo) {
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        if (StringUtils.isEmpty(courseId)) {
            return R.error().message("添加课程信息失败");
        }
        return R.ok().data("courseId", courseId);
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("/{courseId}")
    public R getCourseInfoById(@ApiParam(name = "courseId", value = "课程ID", required = true)
                                   @PathVariable("courseId") String courseId) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfoVoById(courseId);
        return R.ok().data("item", courseInfoVo);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "更新课程")
    @PutMapping("/{courseId}")
    public R updateCourseInfoById(@ApiParam(name = "courseId", value = "课程ID", required = true)
                                      @PathVariable("courseId") String courseId,
                                  @ApiParam(name = "courseInfoVo", value = "课程基本信息", required = true)
                                      @RequestBody CourseInfoVo courseInfoVo) {
        courseInfoVo.setId(courseId);
        courseService.updateCourseInfoVoById(courseInfoVo);
        return R.ok();
    }

    @ApiOperation(value = "根据ID获取课程发布信息")
    @GetMapping("/publish/{id}")
    public R getCoursePublishVoById(@ApiParam(name = "id", value = "课程ID", required = true)
                                        @PathVariable("id") String id) {
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);
        return R.ok().data("item", coursePublishVo);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "发布课程")
    @PutMapping("/publish/{id}")
    public R publishCourse(@ApiParam(name = "id", value = "课程ID", required = true) @PathVariable("id") String id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        if (courseService.updateById(course)) {
            return R.ok().message("课程发布成功");
        }
        return R.error().message("课程发布失败");
    }

    @ApiOperation(value = "条件组合分页查询")
    @PostMapping("/query/{current}/{limit}")
    public R pageQuery(@ApiParam(name = "current", value = "当前页码", required = true) @PathVariable("current") Long current,
                       @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable("limit") Long limit,
                       @ApiParam(name = "courseQuery", value = "查询对象") @RequestBody CourseQuery courseQuery) {
        Page<Course> page = new Page<>(current, limit);
        courseService.pageQuery(page, courseQuery);
        long total = page.getTotal();
        List<Course> courseList = page.getRecords();
        return R.ok().data("total", total).data("items", courseList);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "根据 ID 删除课程")
    @DeleteMapping("/{id}")
    public R deleteCourse(@ApiParam(name = "id", value = "课程ID", required = true) @PathVariable("id") String id) {
        if (courseService.deleteCourse(id)) {
            return R.ok().message("删除课程成功");
        }
        return R.error().message("删除课程失败");
    }
}


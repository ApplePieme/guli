package com.applepieme.edu.controller;

import com.applepieme.edu.entity.Course;
import com.applepieme.edu.entity.Teacher;
import com.applepieme.edu.entity.query.TeacherQuery;
import com.applepieme.edu.service.CourseService;
import com.applepieme.edu.service.TeacherService;
import com.applepieme.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-09-29
 */
@RestController
@RequestMapping("/edu/teacher")
public class TeacherController {
    @Resource
    private TeacherService teacherService;

    @Resource
    private CourseService courseService;

    @ApiOperation(value = "前台根据讲师ID查询讲师信息和所讲课程")
    @GetMapping("/details/{id}")
    public R getTeacherAndCourseListById(@PathVariable("id") String id) {
        Teacher teacher = teacherService.getById(id);
        List<Course> courses = courseService.listCoursesByTeacherId(id);
        return R.ok().data("teacher", teacher).data("courses", courses);
    }

    @ApiOperation(value = "前台分页查询讲师")
    @GetMapping("/front/{current}/{limit}")
    public R listPageTeachersForFront(@PathVariable("current") Long current,
                                      @PathVariable("limit") Long limit) {
        Map<String, Object> map = teacherService.listPageTeachersForFront(new Page<>(current, limit));
        return R.ok().data(map);
    }

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("")
    public R list() {
        List<Teacher> teachers = teacherService.list(null);
        return R.ok().data("teachers", teachers);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("/{id}")
    public R delete(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable("id") String id) {
        boolean removed = teacherService.removeById(id);
        if (removed) {
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping("/page/{current}/{limit}")
    public R page(@ApiParam(name = "current", value = "当前页码", required = true) @PathVariable("current") Long current,
                  @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable("limit") Long limit) {
        Page<Teacher> page = new Page<>(current, limit);
        teacherService.page(page, null);
        long total = page.getTotal();
        List<Teacher> teachers = page.getRecords();
        return R.ok().data("total", total).data("teachers", teachers);
    }

    @ApiOperation(value = "组合条件分页讲师列表")
    @PostMapping("/query/{current}/{limit}")
    public R query(@ApiParam(name = "current", value = "当前页码", required = true) @PathVariable("current") Long current,
                   @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable("limit") Long limit,
                   @ApiParam(name = "teacherQuery", value = "查询对象")
                       @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<Teacher> page = new Page<>(current, limit);
        teacherService.queryPage(page, teacherQuery);
        long total = page.getTotal();
        List<Teacher> teachers = page.getRecords();
        return R.ok().data("total", total).data("teachers", teachers);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "新增讲师")
    @PostMapping("/save")
    public R save(@ApiParam(name = "teacher", value = "讲师对象", required = true) @RequestBody Teacher teacher) {
        if (teacherService.save(teacher)) {
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("/{id}")
    public R getById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable("id") String id) {
        Teacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }

    @CacheEvict(cacheNames = {"index"}, allEntries = true)
    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("/{id}")
    public R updateById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable("id") String id,
                        @ApiParam(name = "teacher", value = "讲师对象", required = true) @RequestBody Teacher teacher) {
        teacher.setId(id);
        if (teacherService.updateById(teacher)) {
            return R.ok();
        }
        return R.error();
    }

}


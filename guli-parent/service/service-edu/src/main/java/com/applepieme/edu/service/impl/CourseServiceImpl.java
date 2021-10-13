package com.applepieme.edu.service.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.edu.entity.Course;
import com.applepieme.edu.entity.CourseDescription;
import com.applepieme.edu.entity.query.CourseQuery;
import com.applepieme.edu.entity.query.CourseQueryFront;
import com.applepieme.vo.CourseDetailsVo;
import com.applepieme.edu.entity.vo.CourseInfoVo;
import com.applepieme.edu.entity.vo.CoursePublishVo;
import com.applepieme.edu.mapper.CourseMapper;
import com.applepieme.edu.service.ChapterService;
import com.applepieme.edu.service.CourseDescriptionService;
import com.applepieme.edu.service.CourseService;
import com.applepieme.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Resource
    private CourseDescriptionService courseDescriptionService;

    @Resource
    private ChapterService chapterService;

    @Resource
    private VideoService videoService;

    @Override
    public void updateCourseBuyCount(String courseId) {
        Course course = baseMapper.selectById(courseId);
        course.setBuyCount(course.getBuyCount() + 1);
        baseMapper.updateById(course);
    }

    @Override
    public void updateCourseViewCount(String id) {
        Course course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
    }

    @Override
    public CourseDetailsVo getCourseDetailsVoById(String id) {
        return baseMapper.getCourseDetailsVoById(id);
    }

    @Override
    public Map<String, Object> listPageCoursesForFront(Page<Course> page, CourseQueryFront courseQueryFront) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseQueryFront.getTitle())) {
            queryWrapper.eq("title", courseQueryFront.getTitle());
        }
        if (!StringUtils.isEmpty(courseQueryFront.getTeacherId())) {
            queryWrapper.eq("teacher_id", courseQueryFront.getTeacherId());
        }
        if (!StringUtils.isEmpty(courseQueryFront.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", courseQueryFront.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQueryFront.getSubjectId())) {
            queryWrapper.eq("subject_id", courseQueryFront.getSubjectId());
        }
        /* 根据销量排序 */
        if (!StringUtils.isEmpty(courseQueryFront.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }
        /* 根据时间排序 */
        if (!StringUtils.isEmpty(courseQueryFront.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }
        /* 根据价格排序 */
        if (!StringUtils.isEmpty(courseQueryFront.getPriceSort())) {
            queryWrapper.orderByDesc("price");
        }

        baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("items", page.getRecords());
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return map;
    }

    @Override
    public List<Course> listCoursesByTeacherId(String id) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", id);
        queryWrapper.orderByDesc("gmt_modified");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    @Cacheable(cacheNames = {"index"}, key = "'courseList'")
    public List<Course> getHotCourseList() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count", "gmt_create");
        queryWrapper.eq("status", "Normal");
        queryWrapper.last("limit 8");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        // 保存课程的基本信息
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoVo, course);
        if (!this.save(course)) {
            throw new GuliException(20001, "课程信息添加失败");
        }

        // 保存课程的描述信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        if (!courseDescriptionService.save(courseDescription)) {
            throw new GuliException(20001, "课程描述添加失败");
        }

        return course.getId();
    }

    @Override
    public CourseInfoVo getCourseInfoVoById(String courseId) {
        // 返回结果
        CourseInfoVo courseInfoVo = new CourseInfoVo();

        // 获取课程信息
        Course course = this.getById(courseId);
        if (course == null) {
            throw new GuliException(20001, "课程不存在");
        }
        BeanUtils.copyProperties(course, courseInfoVo);

        // 获取课程简介
        CourseDescription courseDescription = courseDescriptionService.getById(courseId);
        BeanUtils.copyProperties(courseDescription, courseInfoVo);

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfoVoById(CourseInfoVo courseInfoVo) {
        // 更新课程表
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoVo, course);
        if (!this.updateById(course)) {
            throw new GuliException(20001, "更新课程信息失败");
        }

        // 更新课程简介表
        CourseDescription courseDescription = new CourseDescription();
        BeanUtils.copyProperties(courseInfoVo, courseDescription);
        if (!courseDescriptionService.updateById(courseDescription)) {
            throw new GuliException(20001, "更新课程简介失败");
        }
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.getCoursePublishVoById(id);
    }

    @Override
    public void pageQuery(Page<Course> page, CourseQuery courseQuery) {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        if (courseQuery == null) {
            baseMapper.selectPage(page, wrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        String status = courseQuery.getStatus();

        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }

        baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean deleteCourse(String courseId) {
        // 删除小节
        videoService.deleteVideoByCourseId(courseId);
        // 删除章节
        chapterService.deleteChapterByCourseId(courseId);
        // 删除描述
        courseDescriptionService.removeById(courseId);

        /*TODO 删除封面*/

        // 删除课程
        return baseMapper.deleteById(courseId) > 0;
    }
}

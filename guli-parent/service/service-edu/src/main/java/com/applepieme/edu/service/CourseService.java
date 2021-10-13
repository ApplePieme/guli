package com.applepieme.edu.service;

import com.applepieme.edu.entity.Course;
import com.applepieme.edu.entity.query.CourseQuery;
import com.applepieme.edu.entity.query.CourseQueryFront;
import com.applepieme.vo.CourseDetailsVo;
import com.applepieme.edu.entity.vo.CourseInfoVo;
import com.applepieme.edu.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
public interface CourseService extends IService<Course> {
    /**
     * 更新课程的销量
     *
     * @param courseId 课程 id
     */
    void updateCourseBuyCount(String courseId);

    /**
     * 更新课程的浏览量
     *
     * @param id 课程id
     */
    void updateCourseViewCount(String id);

    /**
     * 根据课程id获取前台页面所需的课程详情
     *
     * @param id 课程id
     * @return 课程详情对象
     */
    CourseDetailsVo getCourseDetailsVoById(String id);

    /**
     * 前台组合条件分页查询课程
     *
     * @param page 分页对象
     * @param courseQueryFront 查询条件对象
     * @return 课程列表和相关分页数据
     */
    Map<String, Object> listPageCoursesForFront(Page<Course> page, CourseQueryFront courseQueryFront);

    /**
     * 根据讲师 ID 查询课程
     *
     * @param id 讲师 id
     * @return 该讲师的课程列表
     */
    List<Course> listCoursesByTeacherId(String id);

    /**
     * 根据观看人数和创建时间查询前 8 条热门课程
     *
     * @return 热门课程列表
     */
    List<Course> getHotCourseList();

    /**
     * 保存课程和课程描述信息
     *
     * @param courseInfoVo CourseInfoVo
     * @return 新生成的课程 id
     */
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据课程id获取课程信息
     *
     * @param courseId 课程id
     * @return 封装的课程信息对象
     */
    CourseInfoVo getCourseInfoVoById(String courseId);

    /**
     * 根据课程id修改课程信息
     *
     * @param courseInfoVo 课程信息对象
     */
    void updateCourseInfoVoById(CourseInfoVo courseInfoVo);

    /**
     * 课程发布信息查询
     *
     * @param id 课程 id
     * @return 封装课程发布确认信息的对象
     */
    CoursePublishVo getCoursePublishVoById(String id);

    /**
     * 条件组合分页查询
     *
     * @param page 分页对象
     * @param courseQuery 查询对象
     */
    void pageQuery(Page<Course> page, CourseQuery courseQuery);

    /**
     * 删除课程
     * 删除课程会同时删除该课程下的简介、章节和小节
     *
     * @param courseId 课程 ID
     * @return 是否删除成功
     */
    boolean deleteCourse(String courseId);
}

package com.applepieme.edu.mapper;

import com.applepieme.edu.entity.Course;
import com.applepieme.vo.CourseDetailsVo;
import com.applepieme.edu.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 根据id查询前台的课程详情页所需信息
     *
     * @param id 课程 id
     * @return 课程详情页信息对象
     */
    CourseDetailsVo getCourseDetailsVoById(String id);

    /**
     * 课程发布信息查询
     *
     * @param id 课程 id
     * @return 封装课程发布确认信息的对象
     */
    CoursePublishVo getCoursePublishVoById(String id);
}

package com.applepieme.edu.service;

import com.applepieme.edu.entity.Teacher;
import com.applepieme.edu.entity.query.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-09-29
 */
public interface TeacherService extends IService<Teacher> {
    /**
     * 分页查询讲师 用于前台展示
     *
     * @param page page 对象
     * @return page 对象中的所有数据
     */
    Map<String, Object> listPageTeachersForFront(Page<Teacher> page);

    /**
     * 根据讲师排序字段和创建时间获取前 4 个讲师
     *
     * @return 热门讲师列表
     */
    List<Teacher> getHotTeacherList();

    /**
     * 组合条件分页查询讲师列表
     *
     * @param page 分页对象
     * @param teacherQuery 查询条件对象
     */
    void queryPage(Page<Teacher> page, TeacherQuery teacherQuery);
}

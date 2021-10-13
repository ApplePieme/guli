package com.applepieme.edu.service.impl;

import com.applepieme.edu.entity.Teacher;
import com.applepieme.edu.entity.query.TeacherQuery;
import com.applepieme.edu.mapper.TeacherMapper;
import com.applepieme.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-09-29
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Map<String, Object> listPageTeachersForFront(Page<Teacher> page) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort", "gmt_create");
        baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("items", page.getRecords());
        map.put("total", page.getTotal());
        map.put("pages", page.getPages());
        map.put("current", page.getCurrent());
        map.put("size", page.getSize());
        map.put("hasPrevious", page.hasPrevious());
        map.put("hasNext", page.hasNext());
        return map;
    }

    @Override
    @Cacheable(cacheNames = {"index"}, key = "'teacherList'")
    public List<Teacher> getHotTeacherList() {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort", "gmt_create");
        queryWrapper.last("limit 4");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void queryPage(Page<Teacher> page, TeacherQuery teacherQuery) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        // 根据排序字段、创建时间排序
        wrapper.orderByDesc("sort", "gmt_create");

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (level != null) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        baseMapper.selectPage(page, wrapper);
    }
}

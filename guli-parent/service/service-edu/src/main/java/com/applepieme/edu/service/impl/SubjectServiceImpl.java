package com.applepieme.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.applepieme.base.exception.GuliException;
import com.applepieme.edu.entity.Subject;
import com.applepieme.edu.entity.excel.SubjectData;
import com.applepieme.edu.entity.vo.SubjectNestedVo;
import com.applepieme.edu.entity.vo.SubjectVo;
import com.applepieme.edu.listener.SubjectExcelListener;
import com.applepieme.edu.mapper.SubjectMapper;
import com.applepieme.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-02
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    @Override
    public void save(MultipartFile multipartFile, SubjectService subjectService) {
        try {
            EasyExcel.read(multipartFile.getInputStream(), SubjectData.class, new SubjectExcelListener(subjectService))
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20002, "导入课程分类失败");
        }
    }

    @Override
    public List<SubjectNestedVo> nestedList() {
        // 最终封装的数据列表
        List<SubjectNestedVo> result = new ArrayList<>();

        // 获取一级分类列表
        QueryWrapper<Subject> oneWrapper = new QueryWrapper<>();
        // parent_id 等于 0 的就是一级分类
        oneWrapper.eq("parent_id", "0");
        List<Subject> oneSubjectList = this.list(oneWrapper);

        // 获取二级分类列表
        QueryWrapper<Subject> twoWrapper = new QueryWrapper<>();
        // parent_id 不等于 0 的就是二级分类
        twoWrapper.ne("parent_id", "0");
        List<Subject> twoSubjectList = this.list(twoWrapper);

        // 封装一级分类列表
        for (Subject oneSubject : oneSubjectList) {
            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
            // 把遍历出来的每个一级分类 Subject 对象的属性拷贝到 subjectNestedVo 中，即拷贝 id 和 title
            BeanUtils.copyProperties(oneSubject, subjectNestedVo);
            // 把 subjectNestedVo 添加到最终返回的 List 中
            result.add(subjectNestedVo);

            // 封装二级分类列表
            List<SubjectVo> subjectVoList = new ArrayList<>();
            for (Subject twoSubject : twoSubjectList) {
                // 当二级分类的 parent_id 等于 一级分类的 id 时，说明这个二级分类的父级分类是这个一级分类
                if (twoSubject.getParentId().equals(oneSubject.getId())) {
                    SubjectVo subjectVo = new SubjectVo();
                    // 拷贝数据
                    BeanUtils.copyProperties(twoSubject, subjectVo);
                    // 添加到列表中
                    subjectVoList.add(subjectVo);
                }
            }
            // 把二级分类列表添加到一级分类的 children 中
            subjectNestedVo.setChildren(subjectVoList);
        }
        return result;
    }
}

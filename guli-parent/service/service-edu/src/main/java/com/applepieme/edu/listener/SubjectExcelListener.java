package com.applepieme.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.applepieme.base.exception.GuliException;
import com.applepieme.edu.entity.Subject;
import com.applepieme.edu.entity.excel.SubjectData;
import com.applepieme.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author applepieme
 * @date 2021/10/2 20:12
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    private SubjectService subjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(SubjectService subjectService) {
        // 传递subjectService用于操作数据库
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(SubjectData data, AnalysisContext context) {
        if (data == null) {
            throw new GuliException(20001, "Excel表格数据为空");
        }
        Subject oneSubject = this.getOneSubject(subjectService, data.getOneSubjectName());
        if (oneSubject == null) {
            oneSubject = new Subject();
            oneSubject.setTitle(data.getOneSubjectName());
            oneSubject.setParentId("0");
            subjectService.save(oneSubject);
        }

        Subject twoSubject = this.getTwoSubject(subjectService, data.getTwoSubjectName(), oneSubject.getId());
        if (twoSubject == null) {
            twoSubject = new Subject();
            twoSubject.setTitle(data.getTwoSubjectName());
            twoSubject.setParentId(oneSubject.getId());
            subjectService.save(twoSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    /**
     * 用于判断一级分类是否重复
     *
     * @param subjectService subject 业务类 用于操作数据库
     * @param oneSubjectName 一级分类名字
     * @return Subject
     */
    private Subject getOneSubject(SubjectService subjectService, String oneSubjectName) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", oneSubjectName);
        wrapper.eq("parent_id", "0");
        return subjectService.getOne(wrapper);
    }

    /**
     * 用于判断二级分类是否重复
     *
     * @param subjectService subject 业务类 用于操作数据库
     * @param twoSubjectName 二级分类名字
     * @param parentId 父级分类的 id
     * @return Subject
     */
    private Subject getTwoSubject(SubjectService subjectService, String twoSubjectName, String parentId) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", twoSubjectName);
        wrapper.eq("parent_id", parentId);
        return subjectService.getOne(wrapper);
    }
}

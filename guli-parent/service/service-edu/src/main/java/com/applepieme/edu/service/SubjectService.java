package com.applepieme.edu.service;

import com.applepieme.edu.entity.Subject;
import com.applepieme.edu.entity.vo.SubjectNestedVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-02
 */
public interface SubjectService extends IService<Subject> {
    /**
     * 根据Excel表格批量导入课程分类
     *
     * @param multipartFile 表单提交的文件
     * @param subjectService subject 业务类 用于操作数据库
     */
    void save(MultipartFile multipartFile, SubjectService subjectService);

    /**
     * 获取课程分类列表
     *
     * @return List
     */
    List<SubjectNestedVo> nestedList();
}

package com.applepieme.edu.controller;


import com.applepieme.edu.entity.vo.SubjectNestedVo;
import com.applepieme.edu.service.SubjectService;
import com.applepieme.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-02
 */
@RestController
@RequestMapping("/edu/subject")
public class SubjectController {
    @Resource
    private SubjectService subjectService;

    @ApiOperation(value = "Excel批量导入")
    @PostMapping("/save")
    public R save(MultipartFile multipartFile) {
        subjectService.save(multipartFile, subjectService);
        return R.ok().message("导入课程分类成功");
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("")
    public R nestedList() {
        List<SubjectNestedVo> subjectNestedVoList = subjectService.nestedList();
        return R.ok().data("items", subjectNestedVoList);
    }

}


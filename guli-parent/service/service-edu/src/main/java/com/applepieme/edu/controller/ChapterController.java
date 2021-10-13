package com.applepieme.edu.controller;


import com.applepieme.edu.entity.Chapter;
import com.applepieme.edu.entity.vo.ChapterVo;
import com.applepieme.edu.service.ChapterService;
import com.applepieme.util.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
@RestController
@RequestMapping("/edu/chapter")
public class ChapterController {
    @Resource
    private ChapterService chapterService;

    @ApiOperation(value = "嵌套查询章节列表")
    @GetMapping("/list/{courseId}")
    public R nestedListByCourseId(@ApiParam(name = "courseId", value = "课程ID", required = true)
                                      @PathVariable("courseId") String courseId) {
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);
        return R.ok().data("items", chapterVoList);
    }

    @ApiOperation(value = "添加章节")
    @PostMapping("/save")
    public R saveChapter(@ApiParam(name = "chapter", value = "章节对象", required = true)
                             @RequestBody Chapter chapter) {
        if (chapterService.save(chapter)) {
            return R.ok().message("添加章节成功");
        }
        return R.error().message("添加章节失败");
    }

    @ApiOperation(value = "根据 id 查询章节")
    @GetMapping("/{id}")
    public R getChapterById(@ApiParam(name = "id", value = "章节ID", required = true) @PathVariable("id") String id) {
        Chapter chapter = chapterService.getById(id);
        return R.ok().data("item", chapter);
    }

    @ApiOperation(value = "根据 id 修改章节")
    @PutMapping("/{id}")
    public R updateChapterById(@ApiParam(name = "id", value = "章节ID", required = true) @PathVariable("id") String id,
                               @ApiParam(name = "chapter", value = "章节对象", required = true)
                                   @RequestBody Chapter chapter) {
        chapter.setId(id);
        if (chapterService.updateById(chapter)) {
            return R.ok().message("修改章节成功");
        }
        return R.error().message("修改章节失败");
    }

    @ApiOperation(value = "根据 id 删除章节")
    @DeleteMapping("/{id}")
    public R deleteChapterById(@PathVariable("id") String id) {
        if (chapterService.deleteChapterById(id)) {
            return R.ok().message("删除章节成功");
        }
        return R.error().message("删除章节失败");
    }
}


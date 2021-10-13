package com.applepieme.edu.controller;


import com.applepieme.edu.entity.Video;
import com.applepieme.edu.service.VideoService;
import com.applepieme.util.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
@RestController
@RequestMapping("/edu/video")
public class VideoController {
    @Resource
    private VideoService videoService;

    @ApiOperation(value = "添加课时")
    @PostMapping("/save")
    public R saveVideo(@ApiParam(name = "video", value = "课时对象", required = true) @RequestBody Video video) {
        if (videoService.save(video)) {
            return R.ok().message("添加课时成功");
        }
        return R.error().message("添加课时失败");
    }

    @ApiOperation(value = "根据 id 删除课时")
    @DeleteMapping("/{id}")
    public R deleteVideoById(@ApiParam(name = "id", value = "课时ID", required = true) @PathVariable("id") String id) {
        if (videoService.deleteVideoById(id)) {
            return R.ok().message("删除课时成功");
        }
        return R.error().message("删除课时失败");
    }

    @ApiOperation(value = "根据 id 获取课时信息")
    @GetMapping("/{id}")
    public R getVideoById(@ApiParam(name = "id", value = "课时ID", required = true) @PathVariable("id") String id) {
        Video video = videoService.getById(id);
        return R.ok().data("item", video);
    }

    @ApiOperation(value = "根据 id 修改课时信息")
    @PutMapping("/{id}")
    public R updateVideoById(@ApiParam(name = "id", value = "课时ID", required = true) @PathVariable("id") String id,
                             @ApiParam(name = "video", value = "课时信息", required = true) @RequestBody Video video) {
        video.setId(id);
        if (videoService.updateById(video)) {
            return R.ok().message("修改课时信息成功");
        }
        return R.error().message("修改课时信息失败");
    }

}


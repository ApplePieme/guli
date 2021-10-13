package com.applepieme.vod.controller;

import com.aliyuncs.exceptions.ClientException;
import com.applepieme.util.R;
import com.applepieme.vod.service.VideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author applepieme
 * @date 2021/10/4 18:21
 */
@RestController
@RequestMapping("/vod/video")
public class VideoController {
    @Resource
    private VideoService videoService;

    @ApiOperation(value = "获取视频播放凭证")
    @GetMapping("/auth/{id}")
    public R getVideoPlayAuth(@PathVariable("id") String id) throws ClientException {
        String auth = videoService.getVideoPlayAuth(id);
        return R.ok().data("auth", auth);
    }

    @ApiOperation(value = "视频上传")
    @PostMapping("/upload")
    public R uploadVideo(@ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        String videoId = videoService.uploadVideo(file);
        return R.ok().data("videoId", videoId);
    }

    @ApiOperation(value = "根据视频 ID 删除视频")
    @DeleteMapping("/{id}")
    public R deleteVideoById(@ApiParam(name = "id", value = "云端视频id", required = true) @PathVariable("id") String id) {
        videoService.deleteVideoById(id);
        return R.ok().message("删除视频成功");
    }

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("/delete_batch")
    public R removeVideoList(@ApiParam(name = "videoIdList", value = "云端视频id列表", required = true)
                                 @RequestParam("videoIdList") List<String> videoIdList) {
        videoService.removeVideoList(videoIdList);
        return R.ok().message("批量删除视频成功");
    }
}

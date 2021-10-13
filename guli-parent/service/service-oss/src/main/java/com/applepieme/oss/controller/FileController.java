package com.applepieme.oss.controller;

import com.applepieme.oss.service.FileService;
import com.applepieme.util.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件上传
 *
 * @author applepieme
 * @date 2021/10/1 21:05
 */
@RestController
@RequestMapping("/oss/file")
public class FileController {
    @Resource
    private FileService fileService;

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload/{host}")
    public R upload(@ApiParam(name = "multipartFile", value = "文件", required = true)
                        @RequestParam("multipartFile") MultipartFile multipartFile,
                    @ApiParam(name = "host", value = "上传路径", required = true)
                        @PathVariable("host") String host) {
        String uploadUrl = fileService.upload(multipartFile, host);
        return R.ok().data("uploadUrl", uploadUrl);
    }
}

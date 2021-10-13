package com.applepieme.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author applepieme
 * @date 2021/10/1 21:05
 */
public interface FileService {
    /**
     * 文件上传
     *
     * @param multipartFile 文件
     * @param host 上传路径
     * @return 公网访问地址
     */
    String upload(MultipartFile multipartFile, String host);
}

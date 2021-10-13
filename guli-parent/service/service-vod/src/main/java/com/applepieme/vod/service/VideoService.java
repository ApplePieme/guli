package com.applepieme.vod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author applepieme
 * @date 2021/10/4 18:20
 */
public interface VideoService {
    /**
     * 根据视频ID获取播放凭证
     *
     * @param videoId 视频 id
     * @return 视频播放凭证
     */
    String getVideoPlayAuth(String videoId) throws ClientException;

    /**
     * 上传视频
     *
     * @param file 视频文件
     * @return 阿里云视频 ID
     */
    String uploadVideo(MultipartFile file);

    /**
     * 根据 ID 删除云端视频
     *
     * @param id 视频 ID
     */
    void deleteVideoById(String id);

    /**
     * 批量删除云端视频
     *
     * @param videoIdList 视频 ID 列表
     */
    void removeVideoList(List<String> videoIdList);
}

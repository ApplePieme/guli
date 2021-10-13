package com.applepieme.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.applepieme.base.exception.GuliException;
import com.applepieme.vod.service.VideoService;
import com.applepieme.vod.util.AliyunVodSDKUtils;
import com.applepieme.vod.util.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author applepieme
 * @date 2021/10/4 18:20
 */
@Service
public class VideoServiceImpl implements VideoService {
    @Override
    public String getVideoPlayAuth(String videoId) throws ClientException {
        /* 初始化 */
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.ACCESS_KEY_SECRET);
        /* 获取请求对象 */
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        /* 设置视频 ID */
        request.setVideoId(videoId);
        /* 获取响应对象 */
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);
        /* 返回视频播放凭证 */
        return response.getPlayAuth();
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            /*文件输入流*/
            InputStream inputStream = file.getInputStream();
            /*上传的视频原文件名*/
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                throw new GuliException(20001, "上传文件名为空");
            }
            /*视频标题*/
            String videoTitle = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            /*创建文件上传请求对象*/
            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtils.ACCESS_KEY_ID,
                    ConstantPropertiesUtils.ACCESS_KEY_SECRET,
                    videoTitle, originalFilename, inputStream);
            UploadVideoImpl uploadVideo = new UploadVideoImpl();
            /*得到响应对象*/
            UploadStreamResponse response = uploadVideo.uploadStream(request);
            /*获取视频 ID*/
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if(StringUtils.isEmpty(videoId)){
                    throw new GuliException(20001, errorMessage);
                }
            }
            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "视频上传失败");
        }
    }

    @Override
    public void deleteVideoById(String id) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtils.ACCESS_KEY_ID,
                    ConstantPropertiesUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }

    @Override
    public void removeVideoList(List<String> videoIdList) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtils.ACCESS_KEY_ID,
                    ConstantPropertiesUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            /*把 List 中的 ID 转换成用 , 拼接的字符串*/
            String videoIds = org.apache.commons.lang.StringUtils.join(videoIdList.toArray(), ",");
            request.setVideoIds(videoIds);
            client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }
}

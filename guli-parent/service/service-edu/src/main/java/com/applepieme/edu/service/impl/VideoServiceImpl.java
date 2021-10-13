package com.applepieme.edu.service.impl;

import com.applepieme.edu.client.VodClient;
import com.applepieme.edu.entity.Video;
import com.applepieme.edu.mapper.VideoMapper;
import com.applepieme.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Resource
    private VodClient vodClient;

    @Override
    public boolean deleteVideoByCourseId(String courseId) {
        /*根据课程 ID 查询该课程下所有的视频*/
        QueryWrapper<Video> videoIdsQueryWrapper = new QueryWrapper<>();
        videoIdsQueryWrapper.eq("course_id", courseId);
        /*只需要视频 ID 所以只需要查出视频 ID 即可*/
        videoIdsQueryWrapper.select("video_source_id");
        List<Video> videoList = baseMapper.selectList(videoIdsQueryWrapper);

        /*用于存放视频 ID 的列表*/
        List<String> videoIdList = new ArrayList<>();
        for (Video video : videoList) {
            String videoSourceId = video.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)) {
                videoIdList.add(videoSourceId);
            }
        }

        /*调用 vod 模块批量删除视频*/
        if (!videoIdList.isEmpty()) {
            vodClient.removeVideoList(videoIdList);
        }

        /*删除课时*/
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        return baseMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean deleteVideoById(String id) {
        /*根据课时 ID 获取 视频 ID*/
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        /*如果视频 ID 不为空则调用 vod 模块删除视频*/
        if (!StringUtils.isEmpty(videoSourceId)) {
            vodClient.deleteVideoById(videoSourceId);
        }
        /*删除课时*/
        return baseMapper.deleteById(id) > 0;
    }
}

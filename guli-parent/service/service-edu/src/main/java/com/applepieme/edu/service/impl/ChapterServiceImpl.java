package com.applepieme.edu.service.impl;

import com.applepieme.base.exception.GuliException;
import com.applepieme.edu.entity.Chapter;
import com.applepieme.edu.entity.Video;
import com.applepieme.edu.entity.vo.ChapterVo;
import com.applepieme.edu.entity.vo.VideoVo;
import com.applepieme.edu.mapper.ChapterMapper;
import com.applepieme.edu.service.ChapterService;
import com.applepieme.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    @Resource
    private VideoService videoService;

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        // 最终返回结果
        List<ChapterVo> result = new ArrayList<>();

        // 查询章节
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId);
        chapterWrapper.orderByAsc("sort", "id");
        List<Chapter> chapterList = this.list(chapterWrapper);

        // 查询小节
        QueryWrapper<Video> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        videoWrapper.orderByAsc("sort", "id");
        List<Video> videoList = videoService.list(videoWrapper);

        // 把章节添加到返回结果中
        for (Chapter chapter : chapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            result.add(chapterVo);

        //    把小节添加到返回结果中
            List<VideoVo> videoVoList = new ArrayList<>();
            for (Video video : videoList) {
                if (video.getChapterId().equals(chapterVo.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }

        //    把小节列表添加到对应的章节中
            chapterVo.setChildren(videoVoList);
        }

        return result;
    }

    @Override
    public boolean deleteChapterById(String id) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", id);
        if (videoService.count(wrapper) > 0) {
            throw new GuliException(20001, "该章节下存在视频课程，请先删除视频课程");
        }
        return this.removeById(id);
    }

    @Override
    public boolean deleteChapterByCourseId(String courseId) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        return baseMapper.delete(queryWrapper) > 0;
    }
}

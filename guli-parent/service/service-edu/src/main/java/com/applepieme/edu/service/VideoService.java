package com.applepieme.edu.service;

import com.applepieme.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
public interface VideoService extends IService<Video> {
    /**
     * 根据课程 id 删除课时
     *
     * @param courseId 课程 id
     * @return 是否删除成功
     */
    boolean deleteVideoByCourseId(String courseId);

    /**
     * 根据课时 ID 删除课时
     * 同时删除该课时下的视频
     *
     * @param id 课时 ID
     * @return 是否删除成功
     */
    boolean deleteVideoById(String id);
}

package com.applepieme.edu.service;

import com.applepieme.edu.entity.Chapter;
import com.applepieme.edu.entity.vo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-03
 */
public interface ChapterService extends IService<Chapter> {
    /**
     * 嵌套查询章节列表
     *
     * @param courseId 课程 id
     * @return 章节列表对象
     */
    List<ChapterVo> nestedList(String courseId);

    /**
     * 根据 id 删除章节
     *
     * @param id 章节 id
     * @return 是否删除成功
     */
    boolean deleteChapterById(String id);

    /**
     * 根据课程 id 删除章节
     *
     * @param courseId 课程 ID
     * @return 是否删除成功
     */
    boolean deleteChapterByCourseId(String courseId);
}

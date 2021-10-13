package com.applepieme.edu.service;

import com.applepieme.edu.entity.Comment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
public interface CommentService extends IService<Comment> {
    Map<String, Object> listPageCommentsByCourseId(Page<Comment> page, String courseId);
}

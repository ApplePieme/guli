package com.applepieme.edu.controller;


import com.applepieme.edu.client.UserClient;
import com.applepieme.edu.entity.Comment;
import com.applepieme.edu.service.CommentService;
import com.applepieme.util.JwtUtils;
import com.applepieme.util.R;
import com.applepieme.vo.MemberVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
@RestController
@RequestMapping("/edu/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @Resource
    private UserClient userClient;

    @ApiOperation(value = "根据课程id分页查询评论")
    @GetMapping("/{id}/{current}/{limit}")
    public R listPageCommentsByCourseId(@PathVariable("id") String id,
                                        @PathVariable("current") Long current,
                                        @PathVariable("limit") Long limit) {
        Map<String, Object> map = commentService.listPageCommentsByCourseId(new Page<>(current, limit), id);
        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("/save")
    public R saveComment(@RequestBody Comment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return R.error().message("请先登录");
        }
        MemberVo memberVo = userClient.getMemberVoById(memberId);
        comment.setMemberId(memberVo.getId());
        comment.setNickname(memberVo.getNickname());
        comment.setAvatar(memberVo.getAvatar());
        if (commentService.save(comment)) {
            return R.ok().message("评论成功");
        }
        return R.error().message("评论失败");
    }
}


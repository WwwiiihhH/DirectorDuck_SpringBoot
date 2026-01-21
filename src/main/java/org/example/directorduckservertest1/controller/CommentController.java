package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.CommentDTO;
import org.example.directorduckservertest1.dto.CommentRequest;
import org.example.directorduckservertest1.entity.Comment;
import org.example.directorduckservertest1.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 社区/评论
 * 评论管理：为帖子新增评论、查询评论列表、统计评论数量
 *
 * @module 社区模块
 */

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;

    /**
     * 新增评论
     * 为指定帖子新增一条评论（JSON 请求体）
     *
     * @param request 评论请求体（包含 postId、userId、username、content 等字段）
     * @return 评论创建结果（CommentDTO）
     */
    @PostMapping("/create")
    public Result<CommentDTO> createComment(@RequestBody CommentRequest request) {
        try {
            Comment comment = commentService.createComment(request);
            CommentDTO commentDTO = CommentDTO.fromComment(comment);
            return Result.success(commentDTO);
        } catch (Exception e) {
            return Result.error("评论失败：" + e.getMessage());
        }
    }

    /**
     * 获取帖子评论列表
     * 根据帖子ID获取该帖子的全部评论（返回 CommentDTO 列表）
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    @GetMapping("/post/{postId}")
    public Result<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error("获取评论失败：" + e.getMessage());
        }
    }

    /**
     * 获取帖子评论数量
     * 根据帖子ID统计评论总数
     *
     * @param postId 帖子ID
     * @return 评论数量
     */
    @GetMapping("/post/{postId}/count")
    public Result<Long> getCommentCountByPostId(@PathVariable Long postId) {
        try {
            Long count = commentService.getCommentCountByPostId(postId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("获取评论数量失败：" + e.getMessage());
        }
    }
}
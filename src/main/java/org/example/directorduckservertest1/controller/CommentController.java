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

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * 为帖子添加评论
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
     * 获取帖子的所有评论
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
     * 获取帖子的评论数量
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
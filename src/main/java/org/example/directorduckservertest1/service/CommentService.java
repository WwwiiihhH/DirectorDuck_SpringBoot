package org.example.directorduckservertest1.service;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.CommentDTO;
import org.example.directorduckservertest1.dto.CommentRequest;
import org.example.directorduckservertest1.entity.Comment;
import org.example.directorduckservertest1.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    
    public Comment createComment(CommentRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPostId(request.getPostId());
        comment.setUserId(request.getUserId());
        comment.setUsername(request.getUsername());
        return commentRepository.save(comment);
    }
    
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        return comments.stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }
    
    public Long getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
    
    public List<Comment> getAllCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    /**
     * 获取帖子的最新几条评论
     */
    public List<CommentDTO> getLatestCommentsByPostId(Long postId, int limit) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        return comments.stream()
                .limit(limit)
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }
}
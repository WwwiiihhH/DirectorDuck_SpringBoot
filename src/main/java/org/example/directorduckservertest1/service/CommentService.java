package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.dto.CommentDTO;
import org.example.directorduckservertest1.dto.CommentRequest;
import org.example.directorduckservertest1.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(CommentRequest request);
    List<CommentDTO> getCommentsByPostId(Long postId);
    Long getCommentCountByPostId(Long postId);
    List<CommentDTO> getLatestCommentsByPostId(Long postId, int limit);
}

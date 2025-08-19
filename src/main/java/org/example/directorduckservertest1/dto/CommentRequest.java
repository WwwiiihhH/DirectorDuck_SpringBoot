package org.example.directorduckservertest1.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private String content;
    private Long postId;
    private Long userId;
    private String username;
}
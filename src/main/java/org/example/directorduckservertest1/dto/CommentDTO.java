package org.example.directorduckservertest1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;

    public static CommentDTO fromComment(org.example.directorduckservertest1.entity.Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setPostId(comment.getPostId());
        dto.setUserId(comment.getUserId());
        dto.setUsername(comment.getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}

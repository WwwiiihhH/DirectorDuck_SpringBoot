package org.example.directorduckservertest1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.directorduckservertest1.entity.Post;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private Long publisherId;
    private String publisherUsername;
    private LocalDateTime createdAt;
    private Long likeCount;
    private Boolean isLiked;
    private Long commentCount;
    private List<CommentDTO> comments;

    public static PostDTO fromPost(Post post, Long likeCount, Boolean isLiked, Long commentCount, List<CommentDTO> comments) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setPublisherId(post.getPublisherId());
        dto.setPublisherUsername(post.getPublisherUsername());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikeCount(likeCount);
        dto.setIsLiked(isLiked);
        dto.setCommentCount(commentCount);
        dto.setComments(comments);
        return dto;
    }
}

package org.example.directorduckservertest1.service;

import java.util.List;

public interface PostLikeService {
    boolean toggleLike(Long postId, Long userId);
    boolean isUserLikedPost(Long postId, Long userId);
    Long getPostLikeCount(Long postId);
    List<Long> getUserLikedPostIds(Long userId);
}

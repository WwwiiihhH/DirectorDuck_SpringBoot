package org.example.directorduckservertest1.service;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.entity.PostLike;
import org.example.directorduckservertest1.repository.PostLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    
    private final PostLikeRepository postLikeRepository;
    
    /**
     * 切换点赞状态（如果已点赞则取消，如果未点赞则点赞）
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        boolean exists = postLikeRepository.existsByPostIdAndUserId(postId, userId);
        
        if (exists) {
            // 如果已点赞，则取消点赞
            postLikeRepository.deleteByPostIdAndUserId(postId, userId);
            return false;
        } else {
            // 如果未点赞，则添加点赞
            PostLike postLike = new PostLike();
            postLike.setPostId(postId);
            postLike.setUserId(userId);
            postLikeRepository.save(postLike);
            return true;
        }
    }
    
    /**
     * 检查用户是否已点赞某个帖子
     */
    public boolean isUserLikedPost(Long postId, Long userId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }
    
    /**
     * 获取帖子的点赞数
     */
    public Long getPostLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }
    
    /**
     * 获取用户点赞的所有帖子ID
     */
    public List<Long> getUserLikedPostIds(Long userId) {
        return postLikeRepository.findPostIdsByUserId(userId);
    }
}
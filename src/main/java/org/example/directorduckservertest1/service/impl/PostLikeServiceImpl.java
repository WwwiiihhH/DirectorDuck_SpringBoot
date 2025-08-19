package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.entity.PostLike;
import org.example.directorduckservertest1.repository.PostLikeRepository;
import org.example.directorduckservertest1.service.PostLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
    
    private final PostLikeRepository postLikeRepository;
    
    @Transactional
    @Override
    public boolean toggleLike(Long postId, Long userId) {
        boolean exists = postLikeRepository.existsByPostIdAndUserId(postId, userId);
        
        if (exists) {
            postLikeRepository.deleteByPostIdAndUserId(postId, userId);
            return false;
        } else {
            PostLike postLike = new PostLike();
            postLike.setPostId(postId);
            postLike.setUserId(userId);
            postLikeRepository.save(postLike);
            return true;
        }
    }
    
    @Override
    public boolean isUserLikedPost(Long postId, Long userId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }
    
    @Override
    public Long getPostLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }
    
    @Override
    public List<Long> getUserLikedPostIds(Long userId) {
        return postLikeRepository.findPostIdsByUserId(userId);
    }
}

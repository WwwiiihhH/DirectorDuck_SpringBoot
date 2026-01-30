package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.CommentDTO;
import org.example.directorduckservertest1.dto.PostDTO;
import org.example.directorduckservertest1.dto.PostRequest;
import org.example.directorduckservertest1.entity.Post;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.repository.PostRepository;
import org.example.directorduckservertest1.repository.CommentRepository;
import org.example.directorduckservertest1.repository.PostLikeRepository;
import org.example.directorduckservertest1.service.CommentService;
import org.example.directorduckservertest1.service.PostLikeService;
import org.example.directorduckservertest1.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public Post createPost(PostRequest request) {
        Post post = new Post();
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        return postRepository.save(post);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<PostDTO> getAllPostsWithLikeInfo(Long userId) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        Set<Long> userLikedPostIds = userId != null ?
                postLikeService.getUserLikedPostIds(userId).stream().collect(Collectors.toSet()) :
                Set.of();

        return posts.stream()
                .map(post -> {
                    Long likeCount = postLikeService.getPostLikeCount(post.getId());
                    Boolean isLiked = userLikedPostIds.contains(post.getId());
                    Long commentCount = commentService.getCommentCountByPostId(post.getId());
                    List<CommentDTO> latestComments = commentService.getLatestCommentsByPostId(post.getId(), 3);
                    return PostDTO.fromPost(post, likeCount, isLiked, commentCount, latestComments);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Result<String> deletePost(Long postId) {
        if (postId == null) {
            return Result.error("帖子ID不能为空");
        }
        if (!postRepository.existsById(postId)) {
            return Result.error("帖子不存在");
        }

        // 先删关联数据，避免外键约束问题
        commentRepository.deleteByPostId(postId);
        postLikeRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);

        return Result.success("帖子已删除");
    }
}

package org.example.directorduckservertest1.service;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.CommentDTO;
import org.example.directorduckservertest1.dto.PostDTO;
import org.example.directorduckservertest1.dto.PostRequest;
import org.example.directorduckservertest1.entity.Comment;
import org.example.directorduckservertest1.entity.Post;
import org.example.directorduckservertest1.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeService postLikeService;
    private final CommentService commentService; // 添加评论服务

    public Post createPost(PostRequest request) {
        Post post = new Post();
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        return postRepository.save(post);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 获取所有帖子，包含点赞信息和评论信息
     * @param userId 当前用户ID，用于判断是否已点赞
     * @return 包含点赞信息和评论信息的帖子列表
     */
    public List<PostDTO> getAllPostsWithLikeInfo(Long userId) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        // 获取用户点赞的所有帖子ID
        Set<Long> userLikedPostIds = userId != null ?
                postLikeService.getUserLikedPostIds(userId).stream().collect(Collectors.toSet()) :
                Set.of();

        return posts.stream()
                .map(post -> {
                    Long likeCount = postLikeService.getPostLikeCount(post.getId());
                    Boolean isLiked = userLikedPostIds.contains(post.getId());
                    Long commentCount = commentService.getCommentCountByPostId(post.getId());
                    // 获取最新的3条评论
                    List<CommentDTO> latestComments = commentService.getLatestCommentsByPostId(post.getId(), 3);
                    return PostDTO.fromPost(post, likeCount, isLiked, commentCount, latestComments);
                })
                .collect(Collectors.toList());
    }





}
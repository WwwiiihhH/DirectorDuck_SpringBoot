package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.PostDTO;
import org.example.directorduckservertest1.dto.PostRequest;
import org.example.directorduckservertest1.entity.Post;

import java.util.List;

public interface PostService {
    Post createPost(PostRequest request);
    Post save(Post post);
    List<Post> getAllPosts();
    List<PostDTO> getAllPostsWithLikeInfo(Long userId);
    Result<String> deletePost(Long postId);
}

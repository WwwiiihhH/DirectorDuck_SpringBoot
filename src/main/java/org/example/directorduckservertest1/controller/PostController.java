package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.PostDTO;
import org.example.directorduckservertest1.entity.Post;
import org.example.directorduckservertest1.service.PostLikeService;
import org.example.directorduckservertest1.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    private static final String UPLOAD_DIR = "D:/DirectorDuckPostImg/";

//    // 修改为云服务器存储路径
//    private static final String UPLOAD_DIR = "/www/wwwroot/images/";

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestParam("publisherId") Long publisherId,
            @RequestParam("publisherUsername") String publisherUsername,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        Post post = new Post();
        post.setContent(content);
        post.setPublisherId(publisherId);
        post.setPublisherUsername(publisherUsername);

        if (image != null && !image.isEmpty()) {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, image.getBytes());

            String imageUrl = "/uploads/" + fileName;
            post.setImageUrl(imageUrl);

//            // 修改为云服务器的访问URL
//            String imageUrl = "http://59.110.16.30:8080/images/" + fileName;
//            post.setImageUrl(imageUrl);


        }

        Post savedPost = postService.save(post);
        return ResponseEntity.ok(savedPost);
    }

//
//    // 确保使用云服务器存储路径
//    private static final String UPLOAD_DIR = "/www/wwwroot/images/";
//
//    @PostMapping("/create")
//    public ResponseEntity<Post> createPost(
//            @RequestParam("content") String content,
//            @RequestParam("publisherId") Long publisherId,
//            @RequestParam("publisherUsername") String publisherUsername,
//            @RequestParam(value = "image", required = false) MultipartFile image
//    ) throws IOException {
//
//        Post post = new Post();
//        post.setContent(content);
//        post.setPublisherId(publisherId);
//        post.setPublisherUsername(publisherUsername);
//
//        if (image != null && !image.isEmpty()) {
//            File dir = new File(UPLOAD_DIR);
//            if (!dir.exists()) {
//                dir.mkdirs(); // 确保目录存在
//            }
//
//            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//            Path filePath = Paths.get(UPLOAD_DIR, fileName);
//            Files.write(filePath, image.getBytes());
//
//            // *** 修改这里：返回相对路径 ***
//            // 这样前端就知道应该用 /images/ 路径访问
//            String imageUrl = "/images/" + fileName; // 返回相对路径
//            post.setImageUrl(imageUrl);
//        }
//
//        Post savedPost = postService.save(post);
//        return ResponseEntity.ok(savedPost);
//    }


    @GetMapping("/list")
    public Result<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return Result.success(posts);
    }

    @GetMapping("/list-with-likes")
    public Result<List<PostDTO>> getAllPostsWithLikes(@RequestParam(value = "userId", required = false) Long userId) {
        List<PostDTO> posts = postService.getAllPostsWithLikeInfo(userId);
        return Result.success(posts);
    }

    @PostMapping("/{postId}/like")
    public Result<String> toggleLike(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            boolean isLiked = postLikeService.toggleLike(postId, userId);
            String message = isLiked ? "点赞成功" : "取消点赞成功";
            return Result.success(message);
        } catch (Exception e) {
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    @GetMapping("/{postId}/like-count")
    public Result<Long> getPostLikeCount(@PathVariable Long postId) {
        Long likeCount = postLikeService.getPostLikeCount(postId);
        return Result.success(likeCount);
    }

    @GetMapping("/{postId}/is-liked")
    public Result<Boolean> isUserLikedPost(@PathVariable Long postId, @RequestParam Long userId) {
        boolean isLiked = postLikeService.isUserLikedPost(postId, userId);
        return Result.success(isLiked);
    }
}

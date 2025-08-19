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
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    // 上传图片保存到外部路径（注意根据你自己的环境修改这个路径）
    private static final String UPLOAD_DIR = "D:/DirectorDuckPostImg/";

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

            // 保存图片访问路径
            String imageUrl = "/uploads/" + fileName;
            post.setImageUrl(imageUrl);
        }

        Post savedPost = postService.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/list")
    public Result<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return Result.success(posts);
    }

    /**
     * 获取所有帖子，包含点赞信息
     * @param userId 当前用户ID（可选参数）
     */
    @GetMapping("/list-with-likes")
    public Result<List<PostDTO>> getAllPostsWithLikes(@RequestParam(value = "userId", required = false) Long userId) {
        List<PostDTO> posts = postService.getAllPostsWithLikeInfo(userId);
        return Result.success(posts);
    }

    /**
     * 点赞/取消点赞帖子
     * @param postId 帖子ID
     * @param userId 用户ID
     */
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

    /**
     * 获取帖子的点赞数
     * @param postId 帖子ID
     */
    @GetMapping("/{postId}/like-count")
    public Result<Long> getPostLikeCount(@PathVariable Long postId) {
        Long likeCount = postLikeService.getPostLikeCount(postId);
        return Result.success(likeCount);
    }

    /**
     * 检查用户是否已点赞某个帖子
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    @GetMapping("/{postId}/is-liked")
    public Result<Boolean> isUserLikedPost(@PathVariable Long postId, @RequestParam Long userId) {
        boolean isLiked = postLikeService.isUserLikedPost(postId, userId);
        return Result.success(isLiked);
    }

    // 配置静态资源映射
    @RestController
    public static class UploadConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:" + UPLOAD_DIR);
        }
    }
}
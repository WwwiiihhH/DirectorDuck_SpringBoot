package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.service.PostService;
import org.springframework.web.bind.annotation.*;

/**
 * 后台管理-帖子管理
 * 帖子管理：删除帖子（管理员操作）
 *
 * @module 后台管理/帖子
 */
@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    /**
     * 删除帖子
     * 管理员删除指定帖子（同时清理点赞与评论）
     *
     * @param id 帖子ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}

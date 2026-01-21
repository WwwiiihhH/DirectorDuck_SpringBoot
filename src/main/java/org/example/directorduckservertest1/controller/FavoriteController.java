package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.FavoritePageDTO;
import org.example.directorduckservertest1.dto.FavoriteQuestionDetailDTO;
import org.example.directorduckservertest1.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 题库/收藏
 * 收藏管理：收藏/取消收藏/判断是否收藏/收藏列表分页查询
 *
 * @module 题库模块
 */
@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 收藏题目
     * 根据题目 UUID 将题目加入指定用户的收藏列表
     *
     * @param uuid 题目UUID
     * @param userId 用户ID（query 参数）
     * @return 操作结果描述
     */
    @PostMapping("/{uuid}")
    public Result<String> addFavorite(@PathVariable String uuid,
                                      @RequestParam Long userId) {
        return favoriteService.addFavorite(userId, uuid);
    }

    /**
     * 取消收藏
     * 根据题目 UUID 从指定用户收藏列表中移除
     *
     * @param uuid 题目UUID
     * @param userId 用户ID（query 参数）
     * @return 操作结果描述
     */
    @DeleteMapping("/{uuid}")
    public Result<String> removeFavorite(@PathVariable String uuid,
                                         @RequestParam Long userId) {
        return favoriteService.removeFavorite(userId, uuid);
    }

    /**
     * 是否已收藏
     * 判断指定用户是否收藏了指定题目
     *
     * @param uuid 题目UUID
     * @param userId 用户ID（query 参数）
     * @return 是否收藏（true/false）
     */
    @GetMapping("/exists/{uuid}")
    public Result<Boolean> exists(@PathVariable String uuid,
                                  @RequestParam Long userId) {
        return favoriteService.exists(userId, uuid);
    }

    /**
     * 收藏列表（分页）
     * 分页查询指定用户的收藏题目详情列表
     *
     * @param userId 用户ID（必填）
     * @param page 页码（可选，默认由服务端处理）
     * @param size 每页大小（可选，默认由服务端处理）
     * @return 收藏分页结果（包含列表与分页信息）
     */
    @GetMapping
    public Result<FavoritePageDTO<FavoriteQuestionDetailDTO>> list(@RequestParam Long userId,
                                                                   @RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) Integer size) {
        return favoriteService.listFavorites(userId, page, size);
    }
}

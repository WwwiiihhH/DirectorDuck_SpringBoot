package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.FavoritePageDTO;
import org.example.directorduckservertest1.dto.FavoriteQuestionDetailDTO;
import org.example.directorduckservertest1.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // 收藏：POST /api/favorites/{uuid}?userId=1
    @PostMapping("/{uuid}")
    public Result<String> addFavorite(@PathVariable String uuid,
                                      @RequestParam Long userId) {
        return favoriteService.addFavorite(userId, uuid);
    }

    // 取消收藏：DELETE /api/favorites/{uuid}?userId=1
    @DeleteMapping("/{uuid}")
    public Result<String> removeFavorite(@PathVariable String uuid,
                                         @RequestParam Long userId) {
        return favoriteService.removeFavorite(userId, uuid);
    }

    // 是否收藏：GET /api/favorites/exists/{uuid}?userId=1
    @GetMapping("/exists/{uuid}")
    public Result<Boolean> exists(@PathVariable String uuid,
                                  @RequestParam Long userId) {
        return favoriteService.exists(userId, uuid);
    }

    // 收藏列表（分页）：GET /api/favorites?userId=1&page=1&size=20
    @GetMapping
    public Result<FavoritePageDTO<FavoriteQuestionDetailDTO>> list(@RequestParam Long userId,
                                                                   @RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) Integer size) {
        return favoriteService.listFavorites(userId, page, size);
    }
}

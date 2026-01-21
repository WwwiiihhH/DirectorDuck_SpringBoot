package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.NoticeDTO;
import org.example.directorduckservertest1.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告
 * 公告管理：公告列表、分类查询、标题搜索、公告详情
 *
 * @module 公告模块
 */
@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 获取全部公告
     * 返回系统内所有公告数据
     *
     * @return 公告列表（NoticeDTO）
     */
    @GetMapping
    public Result<List<NoticeDTO>> getAllNotices() {
        List<NoticeDTO> notices = noticeService.getAllNotices();
        return Result.success(notices);
    }

    /**
     * 按类别获取公告
     * 根据公告类别查询公告列表
     *
     * @param category 公告类别（如：浙江 · 公务员）
     * @return 公告列表
     */
    @GetMapping("/category/{category}")
    public Result<List<NoticeDTO>> getNoticesByCategory(@PathVariable String category) {
        List<NoticeDTO> notices = noticeService.getNoticesByCategory(category);
        return Result.success(notices);
    }

    /**
     * 按标题搜索公告
     * 根据标题关键字模糊搜索公告
     *
     * @param title 标题关键字（query 参数）
     * @return 公告列表
     */
    @GetMapping("/search")
    public Result<List<NoticeDTO>> searchNotices(@RequestParam String title) {
        List<NoticeDTO> notices = noticeService.getNoticesByTitle(title);
        return Result.success(notices);
    }

    /**
     * 获取公告详情
     * 根据公告ID查询公告详情信息
     *
     * @param id 公告ID
     * @return 公告详情（NoticeDTO）
     */
    @GetMapping("/{id}")
    public Result<NoticeDTO> getNoticeById(@PathVariable Integer id) {
        NoticeDTO notice = noticeService.getNoticeById(id);
        if (notice != null) {
            return Result.success(notice);
        } else {
            return Result.error("公告不存在");
        }
    }
}
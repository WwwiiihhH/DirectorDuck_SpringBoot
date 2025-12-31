package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.NoticeDTO;
import org.example.directorduckservertest1.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 获取所有公告
    @GetMapping
    public Result<List<NoticeDTO>> getAllNotices() {
        List<NoticeDTO> notices = noticeService.getAllNotices();
        return Result.success(notices);
    }

    // 按类别获取公告
    @GetMapping("/category/{category}")
    public Result<List<NoticeDTO>> getNoticesByCategory(@PathVariable String category) {
        List<NoticeDTO> notices = noticeService.getNoticesByCategory(category);
        return Result.success(notices);
    }

    // 按标题搜索公告
    @GetMapping("/search")
    public Result<List<NoticeDTO>> searchNotices(@RequestParam String title) {
        List<NoticeDTO> notices = noticeService.getNoticesByTitle(title);
        return Result.success(notices);
    }

    // 获取单个公告详情
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
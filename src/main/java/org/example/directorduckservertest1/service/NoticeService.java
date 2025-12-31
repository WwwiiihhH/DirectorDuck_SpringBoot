package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.dto.NoticeDTO;

import java.util.List;

public interface NoticeService {
    List<NoticeDTO> getAllNotices();
    List<NoticeDTO> getNoticesByCategory(String category);
    List<NoticeDTO> getNoticesByTitle(String title);
    NoticeDTO getNoticeById(Integer id);
}
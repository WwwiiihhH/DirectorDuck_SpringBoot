package org.example.directorduckservertest1.service.impl;

import org.example.directorduckservertest1.dto.NoticeDTO;
import org.example.directorduckservertest1.entity.Notice;
import org.example.directorduckservertest1.repository.NoticeRepository;
import org.example.directorduckservertest1.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Override
    public List<NoticeDTO> getAllNotices() {
        return noticeRepository.findAllByOrderByPublishTimeDesc().stream()
                .map(NoticeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeDTO> getNoticesByCategory(String category) {
        return noticeRepository.findByCategoryOrderByPublishTimeDesc(category).stream()
                .map(NoticeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeDTO> getNoticesByTitle(String title) {
        return noticeRepository.findByTitleContainingOrderByPublishTimeDesc(title).stream()
                .map(NoticeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public NoticeDTO getNoticeById(Integer id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        return notice != null ? NoticeDTO.fromEntity(notice) : null;
    }
}
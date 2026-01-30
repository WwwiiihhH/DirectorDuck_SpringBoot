package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.MockExamResultAdminDTO;
import org.example.directorduckservertest1.entity.MockExamResult;
import org.example.directorduckservertest1.entity.MockExamSession;
import org.example.directorduckservertest1.repository.MockExamResultRepository;
import org.example.directorduckservertest1.repository.MockExamSessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模考（管理员端）
 * 成绩管理：查看每次模考的参与成绩
 *
 * @module 模考管理/成绩
 */
@RestController
@RequestMapping("/api/admin/mock-exams")
@RequiredArgsConstructor
public class AdminMockExamResultController {

    private final MockExamResultRepository resultRepository;
    private final MockExamSessionRepository sessionRepository;

    /**
     * 成绩列表
     * 支持按场次与用户名关键字筛选
     *
     * @param sessionId 场次ID（可选）
     * @param keyword 用户名关键字（可选）
     * @return 成绩列表
     */
    @GetMapping("/results")
    public Result<List<MockExamResultAdminDTO>> list(@RequestParam(required = false) Long sessionId,
                                                     @RequestParam(required = false) String keyword) {
        try {
            String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

            List<MockExamResult> results;
            if (sessionId != null && kw != null) {
                results = resultRepository.findBySessionIdAndUsernameContainingOrderBySubmittedAtDesc(sessionId, kw);
            } else if (sessionId != null) {
                results = resultRepository.findBySessionIdOrderBySubmittedAtDesc(sessionId);
            } else if (kw != null) {
                results = resultRepository.findByUsernameContainingOrderBySubmittedAtDesc(kw);
            } else {
                results = resultRepository.findAllByOrderBySubmittedAtDesc();
            }

            // 关联场次标题
            Set<Long> sessionIds = results.stream()
                    .map(MockExamResult::getSessionId)
                    .collect(Collectors.toSet());
            Map<Long, String> sessionTitleMap = sessionRepository.findAllById(sessionIds).stream()
                    .collect(Collectors.toMap(MockExamSession::getId, MockExamSession::getTitle, (a, b) -> a));

            List<MockExamResultAdminDTO> list = results.stream().map(r -> {
                MockExamResultAdminDTO dto = new MockExamResultAdminDTO();
                dto.setId(r.getId());
                dto.setSessionId(r.getSessionId());
                dto.setSessionTitle(sessionTitleMap.get(r.getSessionId()));
                dto.setUserId(r.getUserId());
                dto.setUsername(r.getUsername());
                dto.setTotalQuestions(r.getTotalQuestions());
                dto.setCorrectCount(r.getCorrectCount());
                dto.setScore(r.getScore());
                dto.setSubmittedAt(r.getSubmittedAt());
                return dto;
            }).collect(Collectors.toList());

            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取成绩列表失败：" + e.getMessage());
        }
    }
}

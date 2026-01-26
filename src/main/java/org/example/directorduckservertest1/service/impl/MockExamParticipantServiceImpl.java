package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.MockExamJoinRequest;
import org.example.directorduckservertest1.entity.MockExamParticipant;
import org.example.directorduckservertest1.entity.MockExamSession;
import org.example.directorduckservertest1.repository.MockExamParticipantRepository;
import org.example.directorduckservertest1.repository.MockExamSessionRepository;
import org.example.directorduckservertest1.service.MockExamParticipantService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MockExamParticipantServiceImpl implements MockExamParticipantService {

    private final MockExamParticipantRepository participantRepo;
    private final MockExamSessionRepository sessionRepo;

    @Override
    @Transactional
    public MockExamParticipant join(Long sessionId, MockExamJoinRequest req) {
        MockExamSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("模考场次不存在: " + sessionId));

        // 报名截止校验（建议）
        LocalDateTime now = LocalDateTime.now();
        if (session.getRegisterDeadline() != null && now.isAfter(session.getRegisterDeadline())) {
            throw new IllegalArgumentException("报名已截止");
        }
        // 开考后不允许报名（可选，但推荐）
        if (now.isAfter(session.getStartTime()) || now.isEqual(session.getStartTime())) {
            throw new IllegalArgumentException("已开考，无法报名");
        }

        // 如果已报名，直接返回已报名记录（幂等）
        return participantRepo.findBySessionIdAndUserId(sessionId, req.getUserId())
                .orElseGet(() -> {
                    try {
                        MockExamParticipant p = MockExamParticipant.builder()
                                .sessionId(sessionId)
                                .userId(req.getUserId())
                                .username(req.getUsername().trim())
                                .status(0)
                                .build();
                        return participantRepo.save(p);
                    } catch (DataIntegrityViolationException e) {
                        // 并发重复报名时，唯一键冲突，兜底返回已存在记录
                        return participantRepo.findBySessionIdAndUserId(sessionId, req.getUserId())
                                .orElseThrow(() -> new IllegalArgumentException("报名失败：重复报名"));
                    }
                });
    }

    @Override
    @Transactional
    public void cancel(Long sessionId, Long userId) {
        MockExamParticipant p = participantRepo.findBySessionIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("未报名，无法取消"));

        // 可选：开考后不允许取消
        MockExamSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("模考场次不存在: " + sessionId));
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(session.getStartTime()) || now.isEqual(session.getStartTime())) {
            throw new IllegalArgumentException("已开考，无法取消报名");
        }

        participantRepo.delete(p);
    }

    @Override
    public boolean exists(Long sessionId, Long userId) {
        return participantRepo.existsBySessionIdAndUserId(sessionId, userId);
    }

    @Override
    public long countParticipants(Long sessionId) {
        return participantRepo.countBySessionId(sessionId);
    }
}

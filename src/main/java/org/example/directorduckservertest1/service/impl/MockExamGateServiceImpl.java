package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.MockExamEnterDTO;
import org.example.directorduckservertest1.dto.MockExamStatusDTO;
import org.example.directorduckservertest1.entity.MockExamSession;
import org.example.directorduckservertest1.repository.MockExamParticipantRepository;
import org.example.directorduckservertest1.repository.MockExamSessionRepository;
import org.example.directorduckservertest1.service.MockExamGateService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MockExamGateServiceImpl implements MockExamGateService {

    private final MockExamSessionRepository sessionRepo;
    private final MockExamParticipantRepository participantRepo;

    @Override
    public MockExamStatusDTO getStatus(Long sessionId, Long userId) {
        MockExamSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("模考场次不存在: " + sessionId));

        LocalDateTime now = LocalDateTime.now();
        int status = calcStatus(session.getStartTime(), session.getEndTime(), now);

        Boolean joined = null;
        Boolean canEnter = false;

        if (userId != null) {
            joined = participantRepo.existsBySessionIdAndUserId(sessionId, userId);
            canEnter = joined && status == 1;
        }

        Long remainToStart = null;
        Long remainToEnd = null;

        if (status == 0) {
            remainToStart = Math.max(Duration.between(now, session.getStartTime()).getSeconds(), 0);
        } else if (status == 1) {
            remainToEnd = Math.max(Duration.between(now, session.getEndTime()).getSeconds(), 0);
        }

        return MockExamStatusDTO.builder()
                .sessionId(sessionId)
                .serverNow(now)
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .registerDeadline(session.getRegisterDeadline())
                .status(status)
                .joined(joined)
                .canEnter(canEnter)
                .remainSecondsToStart(remainToStart)
                .remainSecondsToEnd(remainToEnd)
                .build();
    }

    @Override
    public MockExamEnterDTO enter(Long sessionId, Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId不能为空");
        }

        MockExamSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("模考场次不存在: " + sessionId));

        // 必须先报名
        boolean joined = participantRepo.existsBySessionIdAndUserId(sessionId, userId);
        if (!joined) {
            return MockExamEnterDTO.builder()
                    .sessionId(sessionId)
                    .allowed(false)
                    .message("未报名，无法进入考试")
                    .serverNow(LocalDateTime.now())
                    .startTime(session.getStartTime())
                    .endTime(session.getEndTime())
                    .build();
        }

        LocalDateTime now = LocalDateTime.now();

        // 未到开考
        if (now.isBefore(session.getStartTime())) {
            long remain = Math.max(Duration.between(now, session.getStartTime()).getSeconds(), 0);
            return MockExamEnterDTO.builder()
                    .sessionId(sessionId)
                    .allowed(false)
                    .message("未到开考时间，距离开考还有 " + remain + " 秒")
                    .serverNow(now)
                    .startTime(session.getStartTime())
                    .endTime(session.getEndTime())
                    .build();
        }

        // 已结束（超过收卷）
        if (now.isAfter(session.getEndTime()) || now.isEqual(session.getEndTime())) {
            return MockExamEnterDTO.builder()
                    .sessionId(sessionId)
                    .allowed(false)
                    .message("模考已结束，无法进入")
                    .serverNow(now)
                    .startTime(session.getStartTime())
                    .endTime(session.getEndTime())
                    .build();
        }

        // 允许进入（下一步在这里生成试卷并返回题目）
        long remainToEnd = Math.max(Duration.between(now, session.getEndTime()).getSeconds(), 0);
        return MockExamEnterDTO.builder()
                .sessionId(sessionId)
                .allowed(true)
                .message("允许进入考试")
                .serverNow(now)
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .remainSecondsToEnd(remainToEnd)
                .build();
    }

    private int calcStatus(LocalDateTime start, LocalDateTime end, LocalDateTime now) {
        if (now.isBefore(start)) return 0;
        if (now.isAfter(end) || now.isEqual(end)) return 2;
        return 1;
    }
}

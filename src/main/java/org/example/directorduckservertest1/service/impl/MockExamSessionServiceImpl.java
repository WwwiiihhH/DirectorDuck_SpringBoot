package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.MockExamSessionCreateRequest;
import org.example.directorduckservertest1.entity.MockExamSession;
import org.example.directorduckservertest1.mapper.MockExamScoringMapper;
import org.example.directorduckservertest1.repository.MockExamSessionRepository;
import org.example.directorduckservertest1.service.MockExamSessionService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MockExamSessionServiceImpl implements MockExamSessionService {

    private final MockExamSessionRepository repo;
    private final MockExamScoringMapper resultMapper;

    @Override
    public MockExamSession create(MockExamSessionCreateRequest req) {
        validate(req);

        MockExamSession session = MockExamSession.builder()
                .title(req.getTitle().trim())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .registerDeadline(req.getRegisterDeadline())
                .durationMinutes(calcMinutes(req.getStartTime(), req.getEndTime()))
                .status(calcStatus(req.getStartTime(), req.getEndTime(), LocalDateTime.now()))
                .build();

        return repo.save(session);
    }

    @Override
    public MockExamSession update(Long id, MockExamSessionCreateRequest req) {
        validate(req);

        MockExamSession session = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("场次不存在: " + id));

        session.setTitle(req.getTitle().trim());
        session.setStartTime(req.getStartTime());
        session.setEndTime(req.getEndTime());
        session.setRegisterDeadline(req.getRegisterDeadline());
        session.setDurationMinutes(calcMinutes(req.getStartTime(), req.getEndTime()));
        session.setStatus(calcStatus(req.getStartTime(), req.getEndTime(), LocalDateTime.now()));

        return repo.save(session);
    }

    @Override
    public MockExamSession getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("场次不存在: " + id));
    }

    @Override
    public List<MockExamSession> listAll() {
        return repo.findAll();
    }

    private void validate(MockExamSessionCreateRequest req) {
        if (req.getEndTime().isBefore(req.getStartTime()) || req.getEndTime().isEqual(req.getStartTime())) {
            throw new IllegalArgumentException("收卷时间必须晚于开考时间");
        }
        if (req.getRegisterDeadline() != null && req.getRegisterDeadline().isAfter(req.getStartTime())) {
            throw new IllegalArgumentException("报名截止时间不能晚于开考时间");
        }
    }

    private int calcMinutes(LocalDateTime start, LocalDateTime end) {
        long minutes = Duration.between(start, end).toMinutes();
        return (int) Math.max(minutes, 1);
    }

    private int calcStatus(LocalDateTime start, LocalDateTime end, LocalDateTime now) {
        if (now.isBefore(start)) return 0;
        if (now.isAfter(end)) return 2;
        return 1;
    }

    @Override
    public boolean hasUserCompletedExam(Long sessionId, Long userId) {
        if (sessionId == null || userId == null) {
            return false;
        }
        // 调用 Mapper 查询 mock_exam_result 表
        int count = resultMapper.countBySessionAndUser(sessionId, userId);
        return count > 0;
    }


}

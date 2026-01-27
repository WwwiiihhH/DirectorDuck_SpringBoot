package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.dto.MockExamSessionCreateRequest;
import org.example.directorduckservertest1.entity.MockExamSession;

import java.util.List;

public interface MockExamSessionService {
    MockExamSession create(MockExamSessionCreateRequest req);
    MockExamSession update(Long id, MockExamSessionCreateRequest req);
    MockExamSession getById(Long id);
    List<MockExamSession> listAll();
    boolean hasUserCompletedExam(Long sessionId, Long userId);
}

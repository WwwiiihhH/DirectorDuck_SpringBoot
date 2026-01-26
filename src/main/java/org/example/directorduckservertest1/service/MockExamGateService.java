package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.dto.MockExamEnterDTO;
import org.example.directorduckservertest1.dto.MockExamStatusDTO;

public interface MockExamGateService {

    MockExamStatusDTO getStatus(Long sessionId, Long userId);

    MockExamEnterDTO enter(Long sessionId, Long userId);
}

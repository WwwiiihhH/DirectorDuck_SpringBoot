package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.dto.MockExamResultDTO;
import org.example.directorduckservertest1.dto.MockExamSubmitRequest;

public interface MockExamSubmitService {
    MockExamResultDTO submit(Long sessionId, MockExamSubmitRequest req);
    MockExamResultDTO getResult(Long sessionId, Long userId);
    java.util.List<org.example.directorduckservertest1.dto.MockExamWrongQuestionDTO> wrongQuestions(Long sessionId, Long userId);

}

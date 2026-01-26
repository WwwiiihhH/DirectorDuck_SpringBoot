package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.dto.MockExamJoinRequest;
import org.example.directorduckservertest1.entity.MockExamParticipant;

public interface MockExamParticipantService {

    MockExamParticipant join(Long sessionId, MockExamJoinRequest req);

    void cancel(Long sessionId, Long userId);

    boolean exists(Long sessionId, Long userId);

    long countParticipants(Long sessionId);
}

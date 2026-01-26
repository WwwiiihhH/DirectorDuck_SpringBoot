package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.MockExamParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MockExamParticipantRepository extends JpaRepository<MockExamParticipant, Long> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    Optional<MockExamParticipant> findBySessionIdAndUserId(Long sessionId, Long userId);

    long countBySessionId(Long sessionId);
}

package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.MockExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MockExamResultRepository extends JpaRepository<MockExamResult, Long> {
    Optional<MockExamResult> findBySessionIdAndUserId(Long sessionId, Long userId);
}

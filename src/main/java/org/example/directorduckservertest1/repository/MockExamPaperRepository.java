package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.MockExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MockExamPaperRepository extends JpaRepository<MockExamPaper, Long> {
    Optional<MockExamPaper> findBySessionId(Long sessionId);
}

package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.MockExamPaperQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockExamPaperQuestionRepository extends JpaRepository<MockExamPaperQuestion, Long> {

    List<MockExamPaperQuestion> findBySessionIdOrderByOrderIndexAsc(Long sessionId);

    boolean existsBySessionId(Long sessionId);
}

package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.MockExamUserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockExamUserAnswerRepository extends JpaRepository<MockExamUserAnswer, Long> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    List<MockExamUserAnswer> findBySessionIdAndUserIdAndIsCorrectOrderByOrderIndexAsc(Long sessionId, Long userId, Integer isCorrect);
}

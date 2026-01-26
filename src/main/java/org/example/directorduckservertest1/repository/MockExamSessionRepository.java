package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.MockExamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MockExamSessionRepository extends JpaRepository<MockExamSession, Long> {

    List<MockExamSession> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime now);

    List<MockExamSession> findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime now);
}

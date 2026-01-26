package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mock_exam_result",
        uniqueConstraints = @UniqueConstraint(name = "uk_session_user", columnNames = {"session_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="session_id", nullable = false)
    private Long sessionId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name="total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name="correct_count", nullable = false)
    private Integer correctCount;

    @Column(nullable = false, precision = 5, scale = 1)
    private Double score;

    @Column(name="submitted_at", insertable = false, updatable = false)
    private LocalDateTime submittedAt;
}

package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mock_exam_user_answer",
        uniqueConstraints = @UniqueConstraint(name = "uk_session_user_order", columnNames = {"session_id", "user_id", "order_index"}),
        indexes = {
                @Index(name = "idx_session_user", columnList = "session_id,user_id"),
                @Index(name = "idx_session_correct", columnList = "session_id,is_correct")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockExamUserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="session_id", nullable = false)
    private Long sessionId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name="order_index", nullable = false)
    private Integer orderIndex;  // 1..130

    @Column(name="module_code", nullable = false, length = 32)
    private String moduleCode;

    @Column(name="question_uuid", nullable = false, length = 36)
    private String questionUuid;

    @Column(name="user_answer", nullable = false, length = 8)
    private String userAnswer;   // A/B/C/D/""(未作答)

    @Column(name="correct_answer", nullable = false, length = 8)
    private String correctAnswer;

    @Column(name="is_correct", nullable = false)
    private Integer isCorrect;   // 1/0

    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}

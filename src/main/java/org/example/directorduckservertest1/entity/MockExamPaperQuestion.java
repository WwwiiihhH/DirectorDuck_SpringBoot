package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mock_exam_paper_question",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_paper_order", columnNames = {"paper_id", "order_index"}),
                @UniqueConstraint(name = "uk_session_uuid", columnNames = {"session_id", "question_uuid"})
        },
        indexes = {
                @Index(name = "idx_session", columnList = "session_id"),
                @Index(name = "idx_paper", columnList = "paper_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockExamPaperQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="paper_id", nullable = false)
    private Long paperId;

    @Column(name="session_id", nullable = false)
    private Long sessionId;

    @Column(name="module_code", nullable = false, length = 32)
    private String moduleCode;

    @Column(name="question_uuid", nullable = false, length = 36)
    private String questionUuid;

    @Column(name="order_index", nullable = false)
    private Integer orderIndex;

    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}

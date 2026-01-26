package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mock_exam_paper",
        uniqueConstraints = @UniqueConstraint(name = "uk_session", columnNames = {"session_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockExamPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="session_id", nullable = false)
    private Long sessionId;

    @Column(name="total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name="generated_at", insertable = false, updatable = false)
    private LocalDateTime generatedAt;
}

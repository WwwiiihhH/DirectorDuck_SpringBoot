package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mock_exam_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "register_deadline")
    private LocalDateTime registerDeadline;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     * 0 未开始 / 1 进行中 / 2 已结束
     */
    @Column(nullable = false)
    private Integer status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}

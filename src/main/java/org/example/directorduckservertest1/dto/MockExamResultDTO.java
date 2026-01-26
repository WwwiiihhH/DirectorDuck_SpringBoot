package org.example.directorduckservertest1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MockExamResultDTO {
    private Long sessionId;
    private Long userId;
    private String username;

    private Integer totalQuestions;
    private Integer correctCount;

    /** 0.0 ~ 100.0，保留1位小数 */
    private Double score;

    private LocalDateTime submittedAt;
}

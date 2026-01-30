package org.example.directorduckservertest1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MockExamResultAdminDTO {
    private Long id;
    private Long sessionId;
    private String sessionTitle;
    private Long userId;
    private String username;
    private Integer totalQuestions;
    private Integer correctCount;
    private Double score;
    private LocalDateTime submittedAt;
}

package org.example.directorduckservertest1.dto;

import lombok.Builder;
import lombok.Data;
import org.example.directorduckservertest1.entity.MockExamSession;

import java.time.LocalDateTime;

@Data
@Builder
public class MockExamSessionDTO {
    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime registerDeadline;
    private Integer durationMinutes;
    private Integer status;

    public static MockExamSessionDTO fromEntity(MockExamSession e) {
        return MockExamSessionDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .registerDeadline(e.getRegisterDeadline())
                .durationMinutes(e.getDurationMinutes())
                .status(e.getStatus())
                .build();
    }
}

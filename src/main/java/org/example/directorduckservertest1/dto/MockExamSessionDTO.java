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
        // 1. 获取服务器当前时间
        LocalDateTime now = LocalDateTime.now();

        // 2. 现场动态计算状态（核心逻辑）
        int dynamicStatus;
        if (now.isBefore(e.getStartTime())) {
            dynamicStatus = 0; // 未开始
        } else if (now.isAfter(e.getEndTime())) {
            dynamicStatus = 2; // 已结束
        } else {
            dynamicStatus = 1; // 进行中
        }

        return MockExamSessionDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .registerDeadline(e.getRegisterDeadline())
                .durationMinutes(e.getDurationMinutes())
                .status(dynamicStatus) // <--- 这里填入刚刚算出来的 dynamicStatus
                .build();
    }
}
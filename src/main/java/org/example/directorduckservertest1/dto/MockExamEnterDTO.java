package org.example.directorduckservertest1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MockExamEnterDTO {

    private Long sessionId;

    /** 是否允许进入 */
    private Boolean allowed;

    /** 提示信息（允许进入/未到开考/已结束/未报名等） */
    private String message;

    /** 服务端当前时间 */
    private LocalDateTime serverNow;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /** 进行中时距离收卷剩余秒数 */
    private Long remainSecondsToEnd;
}

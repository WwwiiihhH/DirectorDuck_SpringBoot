package org.example.directorduckservertest1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MockExamStatusDTO {

    private Long sessionId;

    /** 服务端当前时间（用于客户端校准倒计时） */
    private LocalDateTime serverNow;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime registerDeadline;

    /**
     * 0 未开始 / 1 进行中 / 2 已结束（以服务端时间计算）
     */
    private Integer status;

    /** 用户是否已报名（需要传 userId 才能判断） */
    private Boolean joined;

    /** 是否允许进入考试（joined==true 且 status==1） */
    private Boolean canEnter;

    /** 距离开考剩余秒数（仅未开始时返回，其他为 null） */
    private Long remainSecondsToStart;

    /** 距离收卷剩余秒数（仅进行中时返回，其他为 null） */
    private Long remainSecondsToEnd;
}

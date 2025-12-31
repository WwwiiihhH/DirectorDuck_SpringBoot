package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 255)
    private String title; // 公告标题

    @Column(name = "category", nullable = false, length = 100)
    private String category; // 类别

    @Column(name = "publish_time", nullable = false)
    private LocalDateTime publishTime; // 发布时间

    @Column(name = "recruit_count")
    private Integer recruitCount; // 招聘人数

    @Column(name = "position_count")
    private Integer positionCount; // 岗位数量

    @Column(name = "apply_time", length = 200)
    private String applyTime; // 报名时间

    @Column(name = "payment_time", length = 200)
    private String paymentTime; // 缴费时间

    @Column(name = "admit_card_time", length = 200)
    private String admitCardTime; // 准考证打印时间

    @Column(name = "exam_time", length = 200)
    private String examTime; // 考试时间

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 公告内容

    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl; // 附件URL

    @Column(name = "image_url", length = 500)
    private String imageUrl; // 图片URL

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 创建时间

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间
}
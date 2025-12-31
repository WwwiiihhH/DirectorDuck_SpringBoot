package org.example.directorduckservertest1.dto;

import lombok.Data;
import org.example.directorduckservertest1.entity.Notice;
import java.time.LocalDateTime;

@Data
public class NoticeDTO {
    private Integer id;
    private String title;
    private String category;
    private String publishTime; // 格式化为字符串
    private Integer recruitCount;
    private Integer positionCount;
    private String applyTime;
    private String paymentTime;
    private String admitCardTime;
    private String examTime;
    private String content; // 完整内容
    private String attachmentUrl;
    private String imageUrl;

    public static NoticeDTO fromEntity(Notice notice) {
        NoticeDTO dto = new NoticeDTO();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setCategory(notice.getCategory());
        dto.setPublishTime(formatLocalDateTime(notice.getPublishTime()));
        dto.setRecruitCount(notice.getRecruitCount());
        dto.setPositionCount(notice.getPositionCount());
        dto.setApplyTime(notice.getApplyTime());
        dto.setPaymentTime(notice.getPaymentTime());
        dto.setAdmitCardTime(notice.getAdmitCardTime());
        dto.setExamTime(notice.getExamTime());
        dto.setContent(notice.getContent());
        dto.setAttachmentUrl(notice.getAttachmentUrl());
        dto.setImageUrl(notice.getImageUrl());
        return dto;
    }

    private static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        String dateTimeStr = dateTime.toString();
        // 处理不同的时间格式，避免StringIndexOutOfBoundsException
        if (dateTimeStr.length() >= 19) {
            return dateTimeStr.substring(0, 19).replace("T", " ");
        } else {
            return dateTimeStr.replace("T", " ");
        }
    }
}
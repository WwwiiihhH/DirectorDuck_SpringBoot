package org.example.directorduckservertest1.dto;

import lombok.Data;

import java.sql.Timestamp;

// 修改BaseQuestion类或创建新的QuestionDetailDTO
@Data
public class QuestionDetailDTO {
    private Long id;
    private String uuid;  // 新增UUID字段
    private String questionText;
    private String questionImage;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String analysis;
    private Byte difficultyLevel;
    private Byte status;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String categoryName; // 题目所属类别名称
}
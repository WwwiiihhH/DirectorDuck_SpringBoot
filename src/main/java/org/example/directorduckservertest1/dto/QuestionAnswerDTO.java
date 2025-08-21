package org.example.directorduckservertest1.dto;

import lombok.Data;

// 如果你想要一个专门用于返回答案解析的DTO
@Data
public class QuestionAnswerDTO {
    private String uuid;
    private String correctAnswer;
    private String analysis;
    private Byte difficultyLevel;
    private String categoryName;
}
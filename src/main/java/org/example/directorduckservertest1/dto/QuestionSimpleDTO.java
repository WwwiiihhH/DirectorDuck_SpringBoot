package org.example.directorduckservertest1.dto;

import lombok.Data;

@Data
public class QuestionSimpleDTO {
    private Long id;
    private String uuid;  // 新增UUID字段
    private String questionText;
    private String questionImage;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
}
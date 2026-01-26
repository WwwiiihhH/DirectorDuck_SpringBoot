package org.example.directorduckservertest1.dto;

import lombok.Data;

@Data
public class MockExamWrongQuestionDTO {

    private Integer orderIndex;
    private String moduleCode;

    private String questionText;
    private String questionImage;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    /** 用户选项（可能为空串） */
    private String userAnswer;

    /** 正确选项（快照） */
    private String correctAnswer;

    /** 解析（题库） */
    private String analysis;
}

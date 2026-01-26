package org.example.directorduckservertest1.dto;

import lombok.Data;

@Data
public class MockExamQuestionDTO {
    private String uuid;
    private Integer subcategoryId;
    private String questionText;
    private String questionImage;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    /** 题型模块：POLITICAL/COMMON/LANGUAGE/QUANT/REASONING/DATA */
    private String moduleCode;

    /** 题目序号（1..130） */
    private Integer orderIndex;
}

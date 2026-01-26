package org.example.directorduckservertest1.dto;

import lombok.Data;

@Data
public class MockExamQuestionDetailDTO {
    private String uuid;
    private String questionText;
    private String questionImage;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    /** 题目解析 */
    private String analysis;
}

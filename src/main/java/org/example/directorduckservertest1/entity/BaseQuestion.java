package org.example.directorduckservertest1.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseQuestion {
    private Long id;
    private Integer subcategoryId;
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
}
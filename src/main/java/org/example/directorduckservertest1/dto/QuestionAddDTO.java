package org.example.directorduckservertest1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 用于添加题目的请求DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAddDTO {
    private Integer categoryId;        // 大类ID
    private Integer subcategoryId;     // 子类ID
    private String questionText;       // 题目内容
    private String questionImage;      // 题目图片URL（可选）
    private String optionA;           // A选项
    private String optionB;           // B选项
    private String optionC;           // C选项
    private String optionD;           // D选项
    private String correctAnswer;     // 正确答案（A/B/C/D）
    private String analysis;          // 答案解析
    private Byte difficultyLevel;     // 难度等级（1-简单, 2-中等, 3-困难）
}
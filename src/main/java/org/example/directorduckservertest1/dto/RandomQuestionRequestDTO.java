package org.example.directorduckservertest1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandomQuestionRequestDTO {
    private Integer categoryId;     // 大类ID（可选）
    private Integer subcategoryId;  // 小类ID（可选）
    private Integer count;          // 题目数量（5-20）
}
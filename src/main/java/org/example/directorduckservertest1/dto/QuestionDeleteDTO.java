package org.example.directorduckservertest1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 用于删除题目的请求DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDeleteDTO {
    private String uuid;              // 题目UUID
}
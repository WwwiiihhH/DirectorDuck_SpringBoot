package org.example.directorduckservertest1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MockExamSubmitRequest {

    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotBlank(message = "username不能为空")
    private String username;

    /**
     * 按试卷顺序提交答案（长度必须等于试卷题目数量：130）
     * 示例：["A","B","","C",...]
     * 空字符串表示未作答
     */
    @NotNull(message = "answers不能为空")
    private List<String> answers;
}

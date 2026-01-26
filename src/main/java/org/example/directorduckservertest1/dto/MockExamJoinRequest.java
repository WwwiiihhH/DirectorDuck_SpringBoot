package org.example.directorduckservertest1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MockExamJoinRequest {

    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotBlank(message = "username不能为空")
    private String username;
}

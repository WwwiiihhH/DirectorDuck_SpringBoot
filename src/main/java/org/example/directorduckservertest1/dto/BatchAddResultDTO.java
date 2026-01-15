package org.example.directorduckservertest1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchAddResultDTO {
    private int total;
    private int success;
    private int fail;
    private List<FailItem> failures = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailItem {
        private int index;     // 第几条（从0开始）
        private String reason; // 失败原因
    }
}

package org.example.directorduckservertest1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PracticeCommentReq {
    private String categoryName;
    private int totalQuestions;
    private int correctCount;
    private int incorrectCount;
    private int unansweredCount;
    private int correctRate;
    private long timeSpentSeconds;

    private List<String> wrongUuids;
    private List<SlowQuestion> topSlowQuestions;

    private long attemptStartEpoch;
    private long attemptEndEpoch;

    @Data
    public static class SlowQuestion {
        private long questionId;
        private long seconds;
    }
}



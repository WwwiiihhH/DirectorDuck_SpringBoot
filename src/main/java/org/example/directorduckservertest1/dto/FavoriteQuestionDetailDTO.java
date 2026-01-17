package org.example.directorduckservertest1.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class FavoriteQuestionDetailDTO {
    private Long id;
    private String uuid;
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

    private String categoryName;

    // 收藏时间（来自 user_favorite_questions.created_at）
    private Timestamp favoritedAt;
}

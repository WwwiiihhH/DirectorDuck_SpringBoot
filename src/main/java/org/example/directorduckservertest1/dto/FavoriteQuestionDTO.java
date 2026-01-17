package org.example.directorduckservertest1.dto;

import lombok.Data;
import java.util.Date;

@Data
public class FavoriteQuestionDTO {
    private String uuid;
    private String questionText;
    private String questionImage;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String categoryName; // 可选：你想展示大类名的话就保留
    private Date favoritedAt;
}

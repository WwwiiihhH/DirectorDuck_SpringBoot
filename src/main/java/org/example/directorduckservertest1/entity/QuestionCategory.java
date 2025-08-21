package org.example.directorduckservertest1.entity;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class QuestionCategory {
    private Integer id;
    private Byte categoryCode;
    private String categoryName;
    private String description;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
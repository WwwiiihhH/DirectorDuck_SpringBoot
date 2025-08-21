package org.example.directorduckservertest1.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionSubcategory {
    private Integer id;
    private Integer categoryId;
    private String subcategoryCode;
    private String subcategoryName;
    private String description;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
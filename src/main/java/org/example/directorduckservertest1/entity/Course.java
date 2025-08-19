package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category")
    private Integer category;
    private String title;
    private String teacher;
    private String description;

    @Column(name = "video_url")
    private String videoUrl;
}
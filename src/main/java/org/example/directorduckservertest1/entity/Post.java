package org.example.directorduckservertest1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    private Long publisherId; // 发布者ID

    @Column(nullable = false)
    private String publisherUsername; // 发布者用户名

    private LocalDateTime createdAt = LocalDateTime.now();
}


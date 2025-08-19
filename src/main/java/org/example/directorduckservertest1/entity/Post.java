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
    private Long publisherId;

    @Column(nullable = false)
    private String publisherUsername;

    private LocalDateTime createdAt = LocalDateTime.now();
}

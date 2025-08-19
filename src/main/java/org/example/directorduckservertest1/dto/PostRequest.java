package org.example.directorduckservertest1.dto;

import lombok.Data;

@Data
public class PostRequest {
    private String content;
    private String imageUrl; // 如果图片直接传链接
}

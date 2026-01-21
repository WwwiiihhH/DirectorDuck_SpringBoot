package org.example.directorduckservertest1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.directorduckservertest1.common.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件/上传
 * 文件上传：题干图片上传（image/*）
 *
 * @module 文件模块
 */
@RestController
@RequestMapping("/api/file") // 定义一个基础路径
@CrossOrigin // 如果需要跨域访问
public class FileUploadController {

    // 从 application.properties 或直接指定路径
    // 推荐使用配置文件，方便管理
    @Value("${upload.quiz.image.path:D:/DirectorDuckQuizImg/}")
    private String uploadDir;

    /**
     * 上传题干图片
     * 上传图片文件并返回服务器保存后的文件名（multipart/form-data）
     *
     * @param file 图片文件（字段名 file）
     * @return 上传结果（成功返回唯一文件名）
     */
    @PostMapping("/upload/quiz-image")
    public Result<String> uploadQuizImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传的文件不能为空");
        }

        // 检查文件类型（可选，但推荐）
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
             return Result.error("只允许上传图片文件 (image/*)");
        }

        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一的文件名，避免冲突
            String originalFilename = file.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);
            // 使用 UUID 生成唯一文件名
            String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension; // 例如: 550e8400-e29b-41d4-a716-446655440000.jpg

            // 构建完整的文件路径
            Path filePath = uploadPath.resolve(uniqueFilename);

            // 将文件保存到指定位置
            file.transferTo(filePath.toFile());

            // 返回文件名（前端需要这个来存储到数据库和构建访问URL）
            // 注意：这里返回的是文件名，前端需要拼接基础URL (e.g., http://.../quizuploads/) 来访问
            return Result.success(uniqueFilename);

        } catch (IOException e) {
            e.printStackTrace(); // 记录错误日志
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }
}
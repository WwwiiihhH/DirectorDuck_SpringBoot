package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.Course;
import org.example.directorduckservertest1.service.CourseService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Result<List<Course>> getAllCourses() {
        List<Course> list = courseService.getAllCourses();
        return Result.success(list);
    }

    // 配置静态资源映射
    @Configuration
    public static class StaticResourceConfiguration implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 映射视频文件目录 - 这是关键配置
            registry.addResourceHandler("/videos/**")
                    .addResourceLocations("file:D:/DirectorDuckCourseVideo/");

            // 如果你还有其他静态资源目录，也可以一并配置
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:D:/DirectorDuckPostImg/");
        }
    }
}
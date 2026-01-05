package org.example.directorduckservertest1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:D:/DirectorDuckCourseVideo/");
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/DirectorDuckPostImg/");
        // 新增配置，用于题干图片
        registry.addResourceHandler("/quizuploads/**") // 新的URL路径
                .addResourceLocations("file:D:/DirectorDuckQuizImg/");

    }
}

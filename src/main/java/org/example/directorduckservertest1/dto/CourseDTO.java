package org.example.directorduckservertest1.dto;

import lombok.Data;
import org.example.directorduckservertest1.entity.Course;

@Data
public class CourseDTO {
    private Integer id;
    private Integer category;
    private String title;
    private String teacher;
    private String description;
    private String videoUrl;
    
    public static CourseDTO fromCourse(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCategory(course.getCategory());
        dto.setTitle(course.getTitle());
        dto.setTeacher(course.getTeacher());
        dto.setDescription(course.getDescription());
        
        // 处理视频URL
        if (course.getVideoUrl() != null) {
            if (course.getVideoUrl().startsWith("http")) {
                // 已经是完整URL
                dto.setVideoUrl(course.getVideoUrl());
            } else {
                // 相对路径，添加完整URL前缀
                dto.setVideoUrl("http://59.110.16.30:8080" + course.getVideoUrl());
            }
        }
        
        return dto;
    }
}
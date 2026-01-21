package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.CourseDTO;
import org.example.directorduckservertest1.entity.Course;
import org.example.directorduckservertest1.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程
 * 课程管理：获取课程列表
 *
 * @module 课程模块
 */

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * 获取全部课程
     * 返回系统内所有课程数据列表
     *
     * @return 课程列表（Course）
     */
    @GetMapping
    public Result<List<Course>> getAllCourses() {
        List<Course> list = courseService.getAllCourses();
        return Result.success(list);
    }


//    @GetMapping
//    public Result<List<CourseDTO>> getAllCourses() {
//        List<Course> courses = courseService.getAllCourses();
//        List<CourseDTO> courseDTOs = courses.stream()
//                .map(CourseDTO::fromCourse)
//                .collect(Collectors.toList());
//        return Result.success(courseDTOs);
//    }

}

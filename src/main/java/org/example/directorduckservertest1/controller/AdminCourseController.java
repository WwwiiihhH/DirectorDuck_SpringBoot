package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.Course;
import org.example.directorduckservertest1.service.CourseService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理-课程管理
 * 课程管理：新增课程、删除课程、课程列表
 *
 * @module 后台管理/课程
 */
@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    /**
     * 获取课程列表
     * 返回全部课程数据
     *
     * @return 课程列表
     */
    @GetMapping
    public Result<List<Course>> list() {
        return Result.success(courseService.getAllCourses());
    }

    /**
     * 新增课程
     * 创建一条课程记录（JSON请求体）
     *
     * @param course 课程信息（title/teacher/description/videoUrl/category）
     * @return 新增结果
     */
    @PostMapping
    public Result<Course> create(@RequestBody Course course) {
        if (course == null) {
            return Result.error("课程信息不能为空");
        }
        if (!StringUtils.hasText(course.getTitle())) {
            return Result.error("课程标题不能为空");
        }
        if (!StringUtils.hasText(course.getTeacher())) {
            return Result.error("授课老师不能为空");
        }
        if (!StringUtils.hasText(course.getVideoUrl())) {
            return Result.error("视频地址不能为空");
        }
        if (course.getCategory() == null) {
            return Result.error("课程分类不能为空");
        }
        Course saved = courseService.createCourse(course);
        return Result.success(saved);
    }

    /**
     * 删除课程
     * 根据课程ID删除课程
     *
     * @param id 课程ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        return courseService.deleteCourse(id);
    }
}

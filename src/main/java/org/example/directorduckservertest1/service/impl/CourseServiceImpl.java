package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.Course;
import org.example.directorduckservertest1.repository.CourseRepository;
import org.example.directorduckservertest1.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Result<String> deleteCourse(Integer courseId) {
        if (courseId == null) {
            return Result.error("课程ID不能为空");
        }
        if (!courseRepository.existsById(courseId)) {
            return Result.error("课程不存在");
        }
        courseRepository.deleteById(courseId);
        return Result.success("课程已删除");
    }
}

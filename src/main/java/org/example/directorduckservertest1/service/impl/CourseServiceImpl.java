package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
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
}

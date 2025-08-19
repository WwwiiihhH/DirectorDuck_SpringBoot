package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}

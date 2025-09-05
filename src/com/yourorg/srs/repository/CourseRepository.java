package com.yourorg.srs.repository;

import com.yourorg.srs.entity.Course;
import java.util.List;

public interface CourseRepository {
    boolean addCourse(Course course);
    boolean updateCourse(Course course);
    boolean deleteCourseByCode(String code);
    Course findByCode(String code);
    List<Course> findAll();
    int countRegistrations(int courseId);
}

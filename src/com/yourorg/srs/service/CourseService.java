package com.yourorg.srs.service;

import com.yourorg.srs.entity.Course;
import java.util.List;

public interface CourseService {
    boolean addCourse(Course course);
    boolean updateCourse(Course course);
    boolean deleteCourseByCode(String code);
    Course findByCode(String code);
    List<Course> findAll();
    int currentEnrollment(int courseId);
}

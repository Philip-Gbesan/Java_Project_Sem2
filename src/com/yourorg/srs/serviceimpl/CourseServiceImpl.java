package com.yourorg.srs.serviceimpl;

import com.yourorg.srs.entity.Course;
import com.yourorg.srs.repository.CourseRepository;
import com.yourorg.srs.service.CourseService;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository repo = new CourseRepositoryImpl();

    @Override
    public boolean addCourse(Course course) { return repo.addCourse(course); }

    @Override
    public boolean updateCourse(Course course) { return repo.updateCourse(course); }

    @Override
    public boolean deleteCourseByCode(String code) { return repo.deleteCourseByCode(code); }

    @Override
    public Course findByCode(String code) { return repo.findByCode(code); }

    @Override
    public List<Course> findAll() { return repo.findAll(); }

    @Override
    public int currentEnrollment(int courseId) { return repo.countRegistrations(courseId); }
}

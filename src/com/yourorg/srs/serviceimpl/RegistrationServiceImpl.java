package com.yourorg.srs.serviceimpl;

import com.yourorg.srs.dto.CourseRegistrationInfo;
import com.yourorg.srs.repository.RegistrationRepository;
import com.yourorg.srs.service.RegistrationService;

import java.util.List;

public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository repo = new RegistrationRepositoryImpl();

    @Override
    public boolean register(int studentId, int courseId) { return repo.register(studentId, courseId); }

    @Override
    public boolean deregister(int studentId, int courseId) { return repo.deregister(studentId, courseId); }

    @Override
    public boolean isRegistered(int studentId, int courseId) { return repo.isRegistered(studentId, courseId); }

    @Override
    public List<CourseRegistrationInfo> findRegistrationsForStudent(int studentId) { return repo.findRegistrationsForStudent(studentId); }
}

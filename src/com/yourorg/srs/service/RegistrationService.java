package com.yourorg.srs.service;

import com.yourorg.srs.dto.CourseRegistrationInfo;
import java.util.List;

public interface RegistrationService {
    boolean register(int studentId, int courseId);
    boolean deregister(int studentId, int courseId);
    boolean isRegistered(int studentId, int courseId);
    List<CourseRegistrationInfo> findRegistrationsForStudent(int studentId);
}

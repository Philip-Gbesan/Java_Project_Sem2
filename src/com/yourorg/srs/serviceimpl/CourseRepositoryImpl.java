package com.yourorg.srs.serviceimpl;

import com.yourorg.srs.entity.Course;
import com.yourorg.srs.repository.CourseRepository;
import com.yourorg.srs.util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {
    @Override
    public boolean addCourse(Course course) {
        if (course.getUnit() <= 0 || course.getCapacity() < 0) return false;
        String sql = "INSERT INTO courses (code, title, unit, capacity) VALUES (?,?,?,?)";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, course.getCode());
            ps.setString(2, course.getTitle());
            ps.setInt(3, course.getUnit());
            ps.setInt(4, course.getCapacity());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error addCourse: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCourse(Course course) {
        if (course.getUnit() <= 0 || course.getCapacity() < 0) return false;
        String sql = "UPDATE courses SET title=?, unit=?, capacity=? WHERE code=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, course.getTitle());
            ps.setInt(2, course.getUnit());
            ps.setInt(3, course.getCapacity());
            ps.setString(4, course.getCode());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error updateCourse: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCourseByCode(String code) {
        String sql = "DELETE FROM courses WHERE code=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error deleteCourseByCode: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Course findByCode(String code) {
        String sql = "SELECT id, code, title, unit, capacity FROM courses WHERE code=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("title"),
                            rs.getInt("unit"),
                            rs.getInt("capacity")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error findByCode: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Course> findAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT id, code, title, unit, capacity FROM courses ORDER BY code";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Course(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getInt("unit"),
                        rs.getInt("capacity")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error findAll: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int countRegistrations(int courseId) {
        String sql = "SELECT COUNT(*) AS cnt FROM registrations WHERE course_id=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (Exception e) {
            System.err.println("Error countRegistrations: " + e.getMessage());
        }
        return 0;
    }
}

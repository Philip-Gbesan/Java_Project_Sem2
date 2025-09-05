package com.yourorg.srs.serviceimpl;

import com.yourorg.srs.dto.CourseRegistrationInfo;
import com.yourorg.srs.repository.RegistrationRepository;
import com.yourorg.srs.util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationRepositoryImpl implements RegistrationRepository {
    @Override
    public boolean register(int studentId, int courseId) {
        String capacitySql = "SELECT capacity FROM courses WHERE id=?";
        String countSql = "SELECT COUNT(*) AS cnt FROM registrations WHERE course_id=?";
        String insertSql = "INSERT INTO registrations (student_id, course_id) VALUES (?,?)";
        try (Connection con = Db.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psCap = con.prepareStatement(capacitySql);
                 PreparedStatement psCnt = con.prepareStatement(countSql);
                 PreparedStatement psIns = con.prepareStatement(insertSql)) {
                psCap.setInt(1, courseId);
                try (ResultSet rsCap = psCap.executeQuery()) {
                    if (!rsCap.next()) { con.rollback(); return false; }
                    int capacity = rsCap.getInt("capacity");

                    psCnt.setInt(1, courseId);
                    int count = 0;
                    try (ResultSet rsCnt = psCnt.executeQuery()) {
                        if (rsCnt.next()) count = rsCnt.getInt("cnt");
                    }
                    if (count >= capacity) {
                        con.rollback();
                        return false; // full
                    }

                    psIns.setInt(1, studentId);
                    psIns.setInt(2, courseId);
                    int rows = psIns.executeUpdate();
                    con.commit();
                    return rows == 1;
                }
            } catch (Exception e) {
                con.rollback();
                System.err.println("Error register: " + e.getMessage());
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.err.println("Conn error register: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deregister(int studentId, int courseId) {
        String sql = "DELETE FROM registrations WHERE student_id=? AND course_id=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error deregister: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isRegistered(int studentId, int courseId) {
        String sql = "SELECT 1 FROM registrations WHERE student_id=? AND course_id=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Error isRegistered: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<CourseRegistrationInfo> findRegistrationsForStudent(int studentId) {
        String sql = "SELECT c.code, c.title, c.unit FROM registrations r " +
                "JOIN courses c ON r.course_id = c.id WHERE r.student_id=? ORDER BY c.code";
        List<CourseRegistrationInfo> list = new ArrayList<>();
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CourseRegistrationInfo(
                            rs.getString("code"),
                            rs.getString("title"),
                            rs.getInt("unit")
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error findRegistrationsForStudent: " + e.getMessage());
        }
        return list;
    }
    
    public void listAllStudentsWithCourses() {
        String sql = "SELECT u.username, c.title AS course_title " +
                     "FROM registrations r " +
                     "JOIN users u ON r.student_id = u.id " +
                     "JOIN courses c ON r.course_id = c.id " +
                     "ORDER BY u.username";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            String currentStudent = "";
            while (rs.next()) {
                String student = rs.getString("username");
                String course = rs.getString("course_title");

                // New student heading
                if (!student.equals(currentStudent)) {
                    System.out.println("\nStudent: " + student);
                    currentStudent = student;
                }

                // Print course under student
                System.out.println("   - " + course);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching students and registrations: " + e.getMessage());
        }
    }

}

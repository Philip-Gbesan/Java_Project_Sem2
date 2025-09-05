package com.yourorg.srs.serviceimpl;
/* @author PHILIP GBESAN */
import com.yourorg.srs.util.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class DataSeeder {

    public static void seedAll() {
        String insertUserSQL = "INSERT IGNORE INTO users (username, password, role, full_name, email) VALUES (?, ?, ?, ?, ?)";
        String insertCourseSQL = "INSERT IGNORE INTO courses (code, title, unit, capacity) VALUES (?, ?, ?, ?)";
        String insertRegSQL = "INSERT IGNORE INTO registrations (student_id, course_id) VALUES (?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(insertUserSQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement courseStmt = conn.prepareStatement(insertCourseSQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement regStmt = conn.prepareStatement(insertRegSQL)) {

            conn.setAutoCommit(false);
            Random rand = new Random();

            // --- Seed 1000 students ---
            for (int i = 1; i <= 1000; i++) {
                String username = "student" + i;
                String password = "c3R1ZGVudDEyMw=="; // base64 from your example
                String role = "STUDENT";
                String fullName = "Student " + i;
                String email = "student" + i + "@example.com";

                userStmt.setString(1, username);
                userStmt.setString(2, password);
                userStmt.setString(3, role);
                userStmt.setString(4, fullName);
                userStmt.setString(5, email);
                userStmt.addBatch();

                if (i % 100 == 0) {
                    userStmt.executeBatch();
                }
            }
            userStmt.executeBatch();

            // --- Seed 100 courses ---
            for (int i = 1; i <= 100; i++) {
                String code = "CSE" + String.format("%03d", i);
                String title = "Course " + i;
                int unit = 2 + rand.nextInt(3);
                int capacity = 30 + rand.nextInt(70);

                courseStmt.setString(1, code);
                courseStmt.setString(2, title);
                courseStmt.setInt(3, unit);
                courseStmt.setInt(4, capacity);
                courseStmt.addBatch();

                if (i % 20 == 0) {
                    courseStmt.executeBatch();
                }
            }
            courseStmt.executeBatch();

            // --- Seed random registrations ---
            // We assume student IDs are sequential (1..1000) and course IDs are sequential (1..100)
            for (int studentId = 1; studentId <= 1000; studentId++) {
                int numCourses = 2 + rand.nextInt(4); // each student gets 2–5 courses
                for (int j = 0; j < numCourses; j++) {
                    int courseId = 1 + rand.nextInt(100); // random course ID
                    regStmt.setInt(1, studentId);
                    regStmt.setInt(2, courseId);
                    regStmt.addBatch();
                }
                if (studentId % 100 == 0) {
                    regStmt.executeBatch();
                }
            }
            regStmt.executeBatch();

            conn.commit();
            System.out.println("✅ Successfully seeded students, courses, and registrations.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


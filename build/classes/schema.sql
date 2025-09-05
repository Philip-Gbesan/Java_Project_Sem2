
-- Run this in MySQL Workbench or the mysql CLI BEFORE starting the app.
CREATE DATABASE IF NOT EXISTS srs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE srs_db;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL, -- base64-encoded for demo (NOT secure)
  role ENUM('ADMIN','STUDENT') NOT NULL,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(16) NOT NULL UNIQUE,
  title VARCHAR(200) NOT NULL,
  unit INT NOT NULL CHECK (unit > 0),
  capacity INT NOT NULL CHECK (capacity >= 0)
);

CREATE TABLE IF NOT EXISTS registrations (
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (student_id, course_id),
  CONSTRAINT fk_reg_user FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_reg_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- Seed admin and a sample student (passwords base64-encoded)
-- admin123 -> YWRtaW4xMjM=
-- student123 -> c3R1ZGVudDEyMw==
INSERT IGNORE INTO users (username, password, role, full_name, email)
VALUES ('admin', 'YWRtaW4xMjM=', 'ADMIN', 'System Admin', 'admin@example.com'),
       ('jane', 'c3R1ZGVudDEyMw==', 'STUDENT', 'Jane Doe', 'jane@example.com');

-- Seed a few courses
INSERT IGNORE INTO courses (code, title, unit, capacity) VALUES
('CSC101', 'Intro to Computer Science', 3, 50),
('MTH201', 'Discrete Mathematics', 3, 40),
('PHY110', 'General Physics', 2, 45);

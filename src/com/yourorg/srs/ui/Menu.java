package com.yourorg.srs.ui;

import com.yourorg.srs.dto.CourseRegistrationInfo;
import com.yourorg.srs.entity.Course;
import com.yourorg.srs.entity.User;
import com.yourorg.srs.service.AuthService;
import com.yourorg.srs.service.CourseService;
import com.yourorg.srs.service.RegistrationService;
import com.yourorg.srs.serviceimpl.AuthServiceImpl;
import com.yourorg.srs.serviceimpl.CourseServiceImpl;
import com.yourorg.srs.serviceimpl.RegistrationRepositoryImpl;
import com.yourorg.srs.serviceimpl.RegistrationServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthService auth = new AuthServiceImpl();
    private final CourseService courses = new CourseServiceImpl();
    private final RegistrationService reg = new RegistrationServiceImpl();
    
    public boolean start() {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthServiceImpl();

        while (true) {
            System.out.println("=== Student / Admin Course Registration System ===");
            System.out.print("Username: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            User user = authService.login(email, password);

            if (user == null) {
                System.out.println("Login failed: Invalid credentials");
                System.out.print("Try again? (y/n): ");
                String retry = sc.nextLine().trim().toLowerCase();

                if (retry.equals("y")) {
                    continue; // loop back to login
                } else {
                    return false; // exit system cleanly
                }
            }

            // If login successful → show menu based on role
            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                return adminMenu( user);
            } else {
                return studentMenu(user);
            }
        }
    }


    private boolean studentMenu(User user) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View profile");
            System.out.println("2. View available courses");
            System.out.println("3. Register for a course");
            System.out.println("4. View registered courses");
            System.out.println("5. Deregister from a course");
            System.out.println("6. Exit");
            System.out.println("7. Switch User");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewProfile(user);
                    break;
                case "2":
                    listCourses();
                    break;
                case "3":
                    registerCourse(user);
                    break;
                case "4":
                    listMyCourses(user);
                    break;
                case "5":
                    deregisterCourse(user);
                    break;
                case "6":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                case "7":
                    return true;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }


    private boolean adminMenu(User user) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add new course");
            System.out.println("2. View all courses");
            System.out.println("3. Edit a course");
            System.out.println("4. Delete a course");
            System.out.println("5. Add new user");
            System.out.println("6. View students and their registered courses");
            System.out.println("7. Switch User");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    listCourses();
                    break;
                case "3":
                    editCourse();
                    break;
                case "4":
                    deleteCourse();
                    break;
                case "5":
                    addUser();
                    break;
                case "6":
                    viewStudentsAndRegistrations();
                    break;
                case "7":
                    return true;
                case "0":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    return false;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }


    private void viewProfile(User user) {
        System.out.println("\n--- Profile ---");
        System.out.println("Name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
    }

    private void listCourses() {
        System.out.println("\n--- Courses ---");
        List<Course> all = courses.findAll();
        if (all.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        for (Course c : all) {
            int enrolled = courses.currentEnrollment(c.getId());
            System.out.printf("%s | %s | %du | capacity %d | enrolled %d%n",
                    c.getCode(), c.getTitle(), c.getUnit(), c.getCapacity(), enrolled);
        }
    }

    private void registerCourse(User user) {
        System.out.print("Enter course code to register: ");
        String code = scanner.nextLine().trim().toUpperCase();
        Course c = courses.findByCode(code);
        if (c == null) { System.out.println("Course not found."); return; }
        int enrolled = courses.currentEnrollment(c.getId());
        if (enrolled >= c.getCapacity()) {
            System.out.println("Course is full.");
            return;
        }
        boolean ok = reg.register(user.getId(), c.getId());
        System.out.println(ok ? "Registered successfully." : "Registration failed or already registered.");
    }

    private void listMyCourses(User user) {
        System.out.println("\n--- My Courses ---");
        List<CourseRegistrationInfo> list = reg.findRegistrationsForStudent(user.getId());
        if (list.isEmpty()) {
            System.out.println("No registrations yet.");
            return;
        }
        list.forEach(item -> System.out.println(item));
    }

    private void deregisterCourse(User user) {
        System.out.print("Enter course code to deregister: ");
        String code = scanner.nextLine().trim().toUpperCase();
        Course c = courses.findByCode(code);
        if (c == null) { System.out.println("Course not found."); return; }
        boolean ok = reg.deregister(user.getId(), c.getId());
        System.out.println(ok ? "Deregistered successfully." : "You are not registered for this course.");
    }

    private void addCourse() {
        System.out.print("Code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Unit (positive): ");
        int unit = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Capacity (>=0): ");
        int capacity = Integer.parseInt(scanner.nextLine().trim());
        Course c = new Course(0, code, title, unit, capacity);
        System.out.println(courses.addCourse(c) ? "Added." : "Failed to add (check constraints/duplicate code).");
    }

    private void editCourse() {
        System.out.print("Enter course code to edit: ");
        String code = scanner.nextLine().trim().toUpperCase();
        Course c = courses.findByCode(code);
        if (c == null) { System.out.println("Course not found."); return; }
        System.out.print("New title (" + c.getTitle() + "): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) title = c.getTitle();
        System.out.print("New unit (" + c.getUnit() + "): ");
        String unitStr = scanner.nextLine().trim();
        int unit = unitStr.isEmpty() ? c.getUnit() : Integer.parseInt(unitStr);
        System.out.print("New capacity (" + c.getCapacity() + "): ");
        String capStr = scanner.nextLine().trim();
        int capacity = capStr.isEmpty() ? c.getCapacity() : Integer.parseInt(capStr);

        c.setTitle(title);
        c.setUnit(unit);
        c.setCapacity(capacity);
        System.out.println(courses.updateCourse(c) ? "Updated." : "Failed to update.");
    }

    private void deleteCourse() {
        System.out.print("Enter course code to delete: ");
        String code = scanner.nextLine().trim().toUpperCase();
        System.out.println(courses.deleteCourseByCode(code) ? "Deleted." : "Failed to delete.");
    }

    private void addUser() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Role (ADMIN/STUDENT): ");
        String role = scanner.nextLine().trim().toUpperCase();
        System.out.print("Full name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        com.yourorg.srs.entity.User u = new com.yourorg.srs.entity.User(0, username, password, role, name, email);
        boolean ok = auth.addUser(u);
        System.out.println(ok ? "User created." : "Failed to create user (maybe username exists).");
    }

    private void viewStudentsAndRegistrations() {
        System.out.println("\n--- Students and their Registrations ---");
        RegistrationRepositoryImpl repo = new RegistrationRepositoryImpl();
        repo.listAllStudentsWithCourses();
    }

}

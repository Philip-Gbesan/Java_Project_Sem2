package com.yourorg.srs.dto;

public class CourseRegistrationInfo {
    private String courseCode;
    private String courseTitle;
    private int unit;

    public CourseRegistrationInfo(String courseCode, String courseTitle, int unit) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.unit = unit;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public int getUnit() { return unit; }

    @Override
    public String toString() {
        return courseCode + " - " + courseTitle + " (" + unit + "u)";
    }
}

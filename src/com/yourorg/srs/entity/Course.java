package com.yourorg.srs.entity;

public class Course {
    private int id;
    private String code;
    private String title;
    private int unit;
    private int capacity;

    public Course(){}

    public Course(int id, String code, String title, int unit, int capacity) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.unit = unit;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getUnit() { return unit; }
    public void setUnit(int unit) { this.unit = unit; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}

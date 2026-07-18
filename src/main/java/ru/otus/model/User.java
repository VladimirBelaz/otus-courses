package ru.otus.model;

public class User {

    private String name;
    private String course;
    private String email;
    private int age;

    public User() {
    }

    public User(String name, String course, String email, int age) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
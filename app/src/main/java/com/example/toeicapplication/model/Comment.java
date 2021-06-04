package com.example.toeicapplication.model;

public class Comment {
    private int id;
    private String content;
    private int rating;
    private long courseId;
    private Long userId;
    private Course course;
    private User user;

    public Comment(int id, String content, int rating, long courseId, Long userId, Course course, User user) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.courseId = courseId;
        this.userId = userId;
        this.course = course;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

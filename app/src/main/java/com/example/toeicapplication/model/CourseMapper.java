package com.example.toeicapplication.model;

import com.google.gson.annotations.SerializedName;

public class CourseMapper {
    @SerializedName("user_id")
    private Long userId;
    private Long score;
    private Float rating;
    @SerializedName("display_name")
    private String displayName;

    public CourseMapper(Long userId, Long score, Float rating, String displayName) {
        this.userId = userId;
        this.score = score;
        this.rating = rating;
        this.displayName = displayName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

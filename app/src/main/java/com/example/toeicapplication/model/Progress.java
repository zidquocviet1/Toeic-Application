package com.example.toeicapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.toeicapplication.db.converter.HashMapConverter;

import java.util.Map;

@Entity(tableName = "progress")
public class Progress {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long courseID;
    private Long remainTime;
    @TypeConverters(HashMapConverter.class)
    private Map<Integer, String> questions;
    private Boolean isCounting;

    public Progress(Long id, Long courseID, Long remainTime, Map<Integer, String> questions, Boolean isCounting) {
        this.id = id;
        this.courseID = courseID;
        this.remainTime = remainTime;
        this.questions = questions;
        this.isCounting = isCounting;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    public Long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(Long remainTime) {
        this.remainTime = remainTime;
    }

    public Map<Integer, String> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<Integer, String> questions) {
        this.questions = questions;
    }

    public Boolean isCounting() {
        return isCounting;
    }

    public void setCounting(Boolean counting) {
        isCounting = counting;
    }
}

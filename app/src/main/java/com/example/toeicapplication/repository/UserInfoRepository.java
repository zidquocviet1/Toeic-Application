package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.Course;

import java.util.List;

import io.reactivex.Single;

public interface UserInfoRepository {
    Single<List<Course>> getAllCourses();
}

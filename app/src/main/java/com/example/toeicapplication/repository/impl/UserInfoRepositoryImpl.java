package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.repository.UserInfoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class UserInfoRepositoryImpl implements UserInfoRepository {
    private final MyDB db;

    @Inject
    public UserInfoRepositoryImpl(MyDB db){
        this.db = db;
    }

    @Override
    public Single<List<Course>> getAllCourses() {
        return db.getCourseDAO().getAllCourses();
    }
}

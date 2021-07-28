package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.relations.CourseWithResults;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.ResultService;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.UserInfoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class UserInfoRepositoryImpl implements UserInfoRepository {
    private final MyDB db;
    private final ResultService resultService;
    private final UserService userService;

    @Inject
    public UserInfoRepositoryImpl(MyDB db, ResultService resultService, UserService userService){
        this.db = db;
        this.resultService = resultService;
        this.userService = userService;
    }

    @Override
    public Single<List<Course>> getAllCourses() {
        return db.getCourseDAO().getAllCourses();
    }

    @Override
    public Single<List<CourseWithResults>> getCourseWithResults() {
        return db.getCourseDAO().getCourseWithResults();
    }

    @Override
    public Observable<Response<MyResponse<List<Result>>>> getResultRemoteByUserId(Long userId) {
        return resultService.getResultByUserI(userId);
    }

    @Override
    public Observable<Response<MyResponse<User>>> findUserRemote(Long userId) {
        return userService.findUser(userId);
    }
}

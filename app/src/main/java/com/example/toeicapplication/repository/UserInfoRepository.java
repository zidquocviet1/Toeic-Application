package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.relations.CourseWithResults;
import com.example.toeicapplication.network.response.MyResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public interface UserInfoRepository {
    Single<List<Course>> getAllCourses();

    Single<List<CourseWithResults>> getCourseWithResults();

    Observable<Response<MyResponse<List<Result>>>> getResultRemoteByUserId(Long userId);

    Observable<Response<MyResponse<User>>> findUserRemote(Long userId);
}

package com.example.toeicapplication.network.service;

import com.example.toeicapplication.model.CourseMapper;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CourseService {
    @GET("course/user_from_course/{courseId}")
    Observable<List<CourseMapper>> getCourseInfo(@Path("courseId") Long courseId);
}

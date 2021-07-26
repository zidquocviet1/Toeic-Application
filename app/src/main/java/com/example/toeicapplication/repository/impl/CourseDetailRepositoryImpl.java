package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.model.CourseMapper;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.CourseService;
import com.example.toeicapplication.repository.CourseDetailRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class CourseDetailRepositoryImpl implements CourseDetailRepository {
    private final CourseService courseService;

    @Inject
    public CourseDetailRepositoryImpl(CourseService courseService){
        this.courseService = courseService;
    }

    @Override
    public Observable<List<CourseMapper>> getCourseInfo(Long courseId) {
        return courseService.getCourseInfo(courseId);
    }
}

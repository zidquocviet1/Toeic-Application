package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.CourseMapper;

import java.util.List;

import io.reactivex.Observable;

public interface CourseDetailRepository {
    Observable<List<CourseMapper>> getCourseInfo(Long courseId);
}

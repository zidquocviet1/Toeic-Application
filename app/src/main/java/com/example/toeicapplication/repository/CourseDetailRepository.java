package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.model.CourseMapper;
import com.example.toeicapplication.network.response.MyResponse;

import java.util.List;

import io.reactivex.Observable;

public interface CourseDetailRepository {
    Observable<List<CourseMapper>> getCourseInfo(Long courseId);

    Observable<MyResponse<List<Comment>>> getTopCommentByCourse(Long courseId, Long volume);
}

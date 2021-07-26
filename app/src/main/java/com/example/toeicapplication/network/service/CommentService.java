package com.example.toeicapplication.network.service;

import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.network.response.MyResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommentService {
    @GET("comment/comment_by_course")
    Observable<MyResponse<List<Comment>>> getTopCommentByCourse(@Query("courseId") Long courseId,
                                                                @Query("volume") Long volume);
}

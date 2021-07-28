package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.network.response.MyResponse;

import io.reactivex.Observable;

public interface CommentRepository {
    Observable<MyResponse<Comment>> postComment(Comment comment);
}

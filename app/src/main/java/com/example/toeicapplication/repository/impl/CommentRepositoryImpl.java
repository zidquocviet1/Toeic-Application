package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.CommentService;
import com.example.toeicapplication.repository.CommentRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class CommentRepositoryImpl implements CommentRepository {
    private final CommentService commentService;

    @Inject
    public CommentRepositoryImpl(CommentService commentService){
        this.commentService = commentService;
    }

    @Override
    public Observable<MyResponse<Comment>> postComment(Comment comment) {
        return commentService.postComment(comment);
    }
}

package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.Result;

import io.reactivex.Completable;

public interface ResultRepository {
    Completable add(Result result);
}

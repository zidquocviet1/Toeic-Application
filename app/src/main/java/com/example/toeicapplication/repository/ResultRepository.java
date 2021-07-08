package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.network.response.MyResponse;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Response;

public interface ResultRepository {
    Completable add(Result result);

    Observable<Response<MyResponse<Result>>> addResultRemote(Result result);
}

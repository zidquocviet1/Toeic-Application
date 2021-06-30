package com.example.toeicapplication.network.service;

import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.network.response.Response;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ResultService {
    @POST(value = "result/add")
    Observable<Response<Result>> add(@Body Result result);
}

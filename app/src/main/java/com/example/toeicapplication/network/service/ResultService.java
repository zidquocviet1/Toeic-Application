package com.example.toeicapplication.network.service;

import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.network.response.MyResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ResultService {
    @POST(value = "result")
    Observable<Response<MyResponse<Result>>> add(@Body Result result);
}

package com.example.toeicapplication.network.service;

import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.network.response.MyResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ResultService {
    @POST(value = "result")
    Observable<Response<MyResponse<Result>>> add(@Body Result result);

    @GET(value = "result/result_by_user")
    Observable<Response<MyResponse<List<Result>>>> getResultByUserI(@Query("userId") Long userId);
}

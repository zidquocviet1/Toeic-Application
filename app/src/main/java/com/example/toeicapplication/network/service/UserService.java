package com.example.toeicapplication.network.service;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @POST(value = "user/login")
    Observable<MyResponse<User>> login(@Body User user);

    @PUT(value  = "user")
    Observable<MyResponse<User>> logout(@Body User user);

    @POST(value = "user/register")
    Observable<MyResponse<User>> register(@Body User user);

    @GET(value = "user/{id}")
    Observable<MyResponse<User>> findUser(@Path("id") Long id);

    @GET(value = "user/leaderboard")
    Observable<Response<MyResponse<List<User>>>> leaderboard(@Header("USER_API_KEY") String key,
                                                                     @Query("courseId") Long courseId);
}

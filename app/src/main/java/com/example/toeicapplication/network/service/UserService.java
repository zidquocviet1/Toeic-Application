package com.example.toeicapplication.network.service;

import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.network.response.GetResponse;
import com.example.toeicapplication.network.response.Response;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @POST(value = "user/login")
    Observable<Response.PostResponse<User>> login(@Body User user);

    @POST(value = "user/register")
    Observable<Response.PostResponse<User>> register(@Body User user);

    @GET(value = "user/{id}")
    Observable<GetResponse<User>> findUser(@Path("id") Long id);
}

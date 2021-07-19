package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;

import io.reactivex.Observable;
import retrofit2.Response;

public interface LoginRepository {
    Observable<Response<MyResponse<User>>> login(User user);

    Observable<MyResponse<User>> signUp(User user);
}

package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.Response;

import io.reactivex.Observable;

public interface LoginRepository {
    Observable<Response<User>> login(User user);

    Observable<Response<User>> signUp(User user);
}

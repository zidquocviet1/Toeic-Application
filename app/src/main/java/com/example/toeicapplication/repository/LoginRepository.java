package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;

import io.reactivex.Observable;

public interface LoginRepository {
    Observable<MyResponse<User>> login(User user);

    Observable<MyResponse<User>> signUp(User user);
}

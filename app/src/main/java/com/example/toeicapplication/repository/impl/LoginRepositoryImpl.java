package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.LoginRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class LoginRepositoryImpl implements LoginRepository {
    private final UserService service;

    @Inject
    public LoginRepositoryImpl(UserService service){
        this.service = service;
    }

    @Override
    public Observable<MyResponse<User>> login(User user) {
        return service.login(user);
    }

    @Override
    public Observable<MyResponse<User>> signUp(User user) {
        return service.register(user);
    }
}

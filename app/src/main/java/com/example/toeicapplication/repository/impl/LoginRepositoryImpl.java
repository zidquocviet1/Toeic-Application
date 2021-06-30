package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.Response;
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
    public Observable<Response<User>> login(User user) {
        return service.login(user);
    }

    @Override
    public Observable<Response<User>> signUp(User user) {
        return service.register(user);
    }
}

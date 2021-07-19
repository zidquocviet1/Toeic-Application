package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.LoginRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class LoginRepositoryImpl implements LoginRepository {
    private final UserService service;

    @Inject
    public LoginRepositoryImpl(UserService service){
        this.service = service;
    }

    @Override
    public Observable<Response<MyResponse<User>>> login(User user) {
        return service.login(user);
    }

    @Override
    public Observable<MyResponse<User>> signUp(User user) {
        return service.register(user);
    }
}

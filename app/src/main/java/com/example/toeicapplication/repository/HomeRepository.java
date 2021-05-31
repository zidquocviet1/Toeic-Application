package com.example.toeicapplication.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.db.model.User;

import java.util.List;

public interface HomeRepository {
    void getAllUsers(MutableLiveData<List<User>> request);

    void addUser(User user);

    void updateUser(User newUser);
}

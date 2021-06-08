package com.example.toeicapplication.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.model.User;
import com.example.toeicapplication.network.response.Response;
import com.example.toeicapplication.utilities.DataState;

public interface LoginRepository {
    void login(User user, Context context, MutableLiveData<DataState<User>> request);

    void signUp(User user, Context context, MutableLiveData<DataState<User>> request);
}

package com.example.toeicapplication.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.network.response.Response;

public interface LoginRepository {
    void login(User user, Context context, MutableLiveData<Response.PostResponse<User>> request);
}

package com.example.toeicapplication.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.repository.LoginRepositoryImpl;
import com.example.toeicapplication.network.response.Response;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> password;
    private final MutableLiveData<String> userError;
    private final MutableLiveData<String> passwordError;
    private final MutableLiveData<User> responseUser;
    private final MutableLiveData<Response.PostResponse<User>> response;

    @Inject
    LoginRepositoryImpl repository;

    @Inject
    public LoginViewModel() {
        userName = new MutableLiveData<>();
        password = new MutableLiveData<>();
        userError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        responseUser = new MutableLiveData<>();
        response = new MutableLiveData<>();

        userName.postValue("");
        password.postValue("");
        userError.postValue("");
        passwordError.postValue("");
        responseUser.postValue(null);
        response.postValue(null);
    }

    public LiveData<String> getUserName() {
        return this.userName;
    }

    public LiveData<String> getPassword() {
        return this.password;
    }

    public LiveData<String> getUserError() {
        return this.userError;
    }

    public LiveData<String> getPasswordError() {
        return this.passwordError;
    }

    public LiveData<User> getUserResponse() {
        return this.responseUser;
    }

    public MutableLiveData<Response.PostResponse<User>> getResponse() {
        return this.response;
    }

    public void setUserName(String userName) {
        this.userName.setValue(userName);
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public void setUserNameError(String message) {
        this.userError.setValue(message);
    }

    public void setPasswordError(String message) {
        this.passwordError.setValue(message);
    }

    public void login(String userName, String password, Context context) {
        repository.login(userName, password, context, this.getResponse());
    }
}

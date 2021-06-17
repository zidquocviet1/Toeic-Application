package com.example.toeicapplication.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.User;
import com.example.toeicapplication.repository.impl.LoginRepositoryImpl;
import com.example.toeicapplication.utilities.DataState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> loginUserName;
    private final MutableLiveData<String> loginPassword;
    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> password;
    private final MutableLiveData<String> rePassword;
    private final MutableLiveData<String> displayName;

    private final MutableLiveData<String> loginUserNameError;
    private final MutableLiveData<String> loginPasswordError;
    private final MutableLiveData<String> userError;
    private final MutableLiveData<String> passwordError;
    private final MutableLiveData<String> displayNameError;
    private final MutableLiveData<String> rePasswordError;

    private final MutableLiveData<Boolean> networkState;

    private final MutableLiveData<DataState<User>> stateResponse;

    @Inject
    LoginRepositoryImpl repository;

    @Inject
    public LoginViewModel() {
        loginUserName = new MutableLiveData<>();
        loginPassword = new MutableLiveData<>();
        userName = new MutableLiveData<>();
        password = new MutableLiveData<>();
        userError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        networkState = new MutableLiveData<>();
        rePassword = new MutableLiveData<>();
        displayName = new MutableLiveData<>();
        displayNameError = new MutableLiveData<>();
        rePasswordError = new MutableLiveData<>();
        loginUserNameError = new MutableLiveData<>();
        loginPasswordError = new MutableLiveData<>();

        stateResponse = new MutableLiveData<>();

        userName.setValue("");
        password.setValue("");
        userError.setValue("");
        passwordError.setValue("");
        rePassword.setValue("");
        displayName.setValue("");
        rePasswordError.setValue("");
        displayNameError.setValue("");
        loginUserName.setValue("");
        loginPassword.setValue("");
        loginUserNameError.setValue("");
        loginPasswordError.setValue("");
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

    public MutableLiveData<String> getLoginUserNameError() {
        return loginUserNameError;
    }

    public MutableLiveData<String> getLoginPasswordError() {
        return loginPasswordError;
    }

    public MutableLiveData<String> getLoginUserName() {
        return loginUserName;
    }

    public MutableLiveData<String> getLoginPassword() {
        return loginPassword;
    }

    public MutableLiveData<String> getDisplayNameError() {
        return displayNameError;
    }

    public MutableLiveData<String> getRePasswordError() {
        return rePasswordError;
    }

    public MutableLiveData<String> getRePassword() {
        return rePassword;
    }

    public MutableLiveData<String> getDisplayName() {
        return displayName;
    }

    public void setUserName(String userName) {
        this.userName.setValue(userName);
    }

    public void setDisplayName(String displayName) {
        this.displayName.setValue(displayName);
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public void setRePassword(String password) {
        this.rePassword.setValue(password);
    }

    public void setUserNameError(String message) {
        this.userError.setValue(message);
    }

    public void setPasswordError(String message) {
        this.passwordError.setValue(message);
    }

    public void setRePasswordError(String message) {
        this.rePasswordError.setValue(message);
    }

    public void setDisplayNameError(String message) {
        this.displayNameError.setValue(message);
    }

    public void setLoginUserName(String userName) {
        this.loginUserName.setValue(userName);
    }

    public void setLoginPassword(String password) {
        this.loginPassword.setValue(password);
    }

    public void setLoginUserNameError(String userName) {
        this.loginUserNameError.setValue(userName);
    }

    public void setLoginPasswordError(String password) {
        this.loginPasswordError.setValue(password);
    }

    public MutableLiveData<DataState<User>> getStateResponse() {
        return stateResponse;
    }

    public MutableLiveData<Boolean> getNetworkState() {
        return networkState;
    }


    public void login(User user, Context context) {
        repository.login(user, context, this.getStateResponse());
    }

    public void signUp(User user, Context context) {
        repository.signUp(user, context, this.getStateResponse());
    }
}

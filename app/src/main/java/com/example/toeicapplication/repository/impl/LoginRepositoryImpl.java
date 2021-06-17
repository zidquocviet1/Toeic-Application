package com.example.toeicapplication.repository.impl;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.R;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.network.response.Response;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.LoginRepository;
import com.example.toeicapplication.utilities.DataState;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginRepositoryImpl implements LoginRepository {
    private final UserService service;

    @Inject
    public LoginRepositoryImpl(UserService service){
        this.service = service;
    }

    @Override
    public void login(User user, Context context, MutableLiveData<DataState<User>> request) {
        service.login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(5, TimeUnit.SECONDS)
                .subscribe(new Observer<Response<User>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        request.postValue(DataState.Loading(null));
                    }

                    @Override
                    public void onNext(@NotNull Response<User> userPostResponse) {
                        if (userPostResponse.isStatus()) {
                            User data = userPostResponse.getData();

                            request.postValue(DataState.Success(data));
                        }else{
                            request.postValue(DataState.Error(userPostResponse.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        request.postValue(DataState.Error(context.getString(R.string.server_error)));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void signUp(User user, Context context, MutableLiveData<DataState<User>> request) {
        service.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(5, TimeUnit.SECONDS)
                .subscribe(new Observer<Response<User>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        request.postValue(DataState.Loading(null));
                    }

                    @Override
                    public void onNext(@NotNull Response<User> userPostResponse) {
                        if (userPostResponse.isStatus()) {
                            User data = userPostResponse.getData();

                            request.postValue(DataState.Success(data));
                        }else{
                            request.postValue(DataState.Error(userPostResponse.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        request.postValue(DataState.Error(context.getString(R.string.server_error)));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

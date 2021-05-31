package com.example.toeicapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.R;
import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.network.response.Response;
import com.example.toeicapplication.network.service.UserService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginRepositoryImpl implements LoginRepository {
    private final UserService service;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public LoginRepositoryImpl(UserService service, CompositeDisposable compositeDisposable){
        this.service = service;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void login(String userName, String password, Context context, MutableLiveData<Response.PostResponse<User>> request) {
        compositeDisposable.add(
                service.login(userName, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(userPostResponse -> {
                            request.postValue(userPostResponse);
                            Log.e("TAG", "Da vao duoc toi day: " + userPostResponse.toString());
                        }, throwable -> {
                            Response.PostResponse<User> res = new Response.PostResponse<>();
                            res.setData(null);
                            res.setStatus(false);
                            res.setMessage(context.getString(R.string.server_error));

                            request.postValue(res);
                            Log.e("TAG", "Cung da vao duoc toi day nhung loi");
                        })
        );
    }

}

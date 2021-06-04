package com.example.toeicapplication.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.R;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.network.response.Response;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.utilities.DataState;

import java.util.concurrent.TimeUnit;

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
    public void login(User user, Context context, MutableLiveData<DataState<User>> request) {
        request.postValue(DataState.Loading(null));

        compositeDisposable.add(
                service.login(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(5, TimeUnit.SECONDS)
                        .subscribe(userPostResponse -> {
                            if (userPostResponse.isStatus()) {
                                User data = userPostResponse.getData();

                                request.postValue(DataState.Success(data));
                            }else{
                                request.postValue(DataState.Error(userPostResponse.getMessage()));
                            }
                        }, throwable -> {
                            request.postValue(DataState.Error(context.getString(R.string.server_error)));
                            throwable.printStackTrace();
//                            Log.e("TAG", "Cung da vao duoc toi day nhung loi");
                        })
        );
    }

}

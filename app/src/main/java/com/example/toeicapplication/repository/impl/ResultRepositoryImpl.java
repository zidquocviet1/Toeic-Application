package com.example.toeicapplication.repository.impl;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.db.dao.ResultDAO;
import com.example.toeicapplication.model.Result;
import com.example.toeicapplication.network.response.Response;
import com.example.toeicapplication.network.service.ResultService;
import com.example.toeicapplication.repository.ResultRepository;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ResultRepositoryImpl implements ResultRepository {
    private final CompositeDisposable cd;
    private final ResultDAO dao;
    private final ResultService resultService;

    @Inject
    public ResultRepositoryImpl(CompositeDisposable cd, ResultDAO dao, ResultService resultService) {
        this.cd = cd;
        this.dao = dao;
        this.resultService = resultService;
    }

    @Override
    public void add(Result result) {
        cd.addAll(
                dao.add(result)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Log.e("TAG", "Insert result successfully!");
                        }),
                resultService.add(result)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resultResponse -> {
                            if (resultResponse.isStatus()) {
                                Log.e("TAG", "Insert result remote successfully!");
                            } else {
                                Log.e("TAG", "Insert result remote failure!");
                            }
                        }, Throwable::printStackTrace)
        );

        cd.dispose();
    }
}

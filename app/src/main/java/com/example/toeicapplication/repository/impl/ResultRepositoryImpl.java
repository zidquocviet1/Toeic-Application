package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.db.dao.ResultDAO;
import com.example.toeicapplication.model.Result;
import com.example.toeicapplication.network.service.ResultService;
import com.example.toeicapplication.repository.ResultRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class ResultRepositoryImpl implements ResultRepository {
    private final ResultDAO dao;
    private final ResultService resultService;

    @Inject
    public ResultRepositoryImpl(ResultDAO dao, ResultService resultService) {
        this.dao = dao;
        this.resultService = resultService;
    }

    @Override
    public Completable add(Result result) {
        return dao.add(result);

//        resultService.add(result)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(resultResponse -> {
//                    if (resultResponse.isStatus()) {
//                        Log.e("TAG", "Insert result remote successfully!");
//                    } else {
//                        Log.e("TAG", "Insert result remote failure!");
//                    }
//                }, Throwable::printStackTrace)
    }
}

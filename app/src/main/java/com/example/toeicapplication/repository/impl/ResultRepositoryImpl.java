package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.db.dao.ResultDAO;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.ResultService;
import com.example.toeicapplication.repository.ResultRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Response;

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
    }

    @Override
    public Observable<Response<MyResponse<Result>>> addResultRemote(Result result) {
        return resultService.add(result);
    }
}

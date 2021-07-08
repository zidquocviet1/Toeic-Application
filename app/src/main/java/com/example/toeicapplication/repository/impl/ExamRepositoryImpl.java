package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.model.entity.Progress;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.repository.ExamRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class ExamRepositoryImpl implements ExamRepository {
    private final MyDB database;

    @Inject
    public ExamRepositoryImpl(MyDB database) {
        this.database = database;
    }

    @Override
    public Single<List<Question>> getListQuestionByCourseID(Long courseID) {
        return database.getQuestionDAO().getListQuestionById(courseID);
    }

    @Override
    public Single<Progress> getProgressByCourseID(Long courseID) {
        return database.getProgressDAO().getByCourseID(courseID);
    }

    @Override
    public Flowable<List<Progress>> getAll() {
        return null;
    }

    @Override
    public Single<Progress> findById(Long aLong) {
        return null;
    }

    @Override
    public Single<Progress> update(Progress progress) {
        return null;
    }

    @Override
    public Completable add(Progress progress) {
        return database.getProgressDAO().add(progress);
    }

    @Override
    public Completable delete(Long id) {
        return database.getProgressDAO().delete(id);
    }
}

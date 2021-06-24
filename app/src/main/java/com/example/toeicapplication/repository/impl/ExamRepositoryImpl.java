package com.example.toeicapplication.repository.impl;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.model.Progress;
import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.repository.ExamRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ExamRepositoryImpl implements ExamRepository {
    private final MyDB database;
    private final CompositeDisposable cd;

    @Inject
    public ExamRepositoryImpl(MyDB database,
                              CompositeDisposable cd) {
        this.database = database;
        this.cd = cd;
    }

    @Override
    public void getListQuestionByCourseID(MutableLiveData<List<Question>> request, Long courseID) {
        cd.add(database.getQuestionDAO().getListQuestionById(courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(request::postValue, throwable -> {
                    Log.e("TAG", "Can't get the data from Question table: " + throwable.getMessage());
                })
        );
    }

    @Override
    public void getProgressByCourseID(MutableLiveData<Progress> request, Long courseID) {
        cd.add(database.getProgressDAO().getByCourseID(courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(request::postValue, throwable -> {
                    Log.e("TAG", "Can't get the data from Progress table: " + throwable.getMessage());
                })
        );
    }

    @Override
    public void add(Progress progress) {
        cd.add(database.getProgressDAO().add(progress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.e("TAG", "Insert the progress successfully"), throwable -> {
                    Log.e("TAG", "Can't get the data from Progress table: " + throwable.getMessage());
                })
        );
    }
}

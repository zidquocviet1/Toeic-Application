package com.example.toeicapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.Result;
import com.example.toeicapplication.repository.ResultRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ResultViewModel extends ViewModel {
    private final MutableLiveData<Result> result;
    private final CompositeDisposable cd;

    private final ResultRepository repository;

    @Inject
    public ResultViewModel(ResultRepository repository) {
        this.repository = repository;

        result = new MutableLiveData<>();
        cd = new CompositeDisposable();
    }

    public MutableLiveData<Result> getResult() {
        return result;
    }

    public void add(Result result) {
        cd.add(repository
                .add(result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.e("TAG", "Insert result successfully!");
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cd.dispose();
    }
}

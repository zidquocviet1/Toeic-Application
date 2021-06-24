package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.Result;
import com.example.toeicapplication.repository.ResultRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ResultViewModel extends ViewModel {
    private final MutableLiveData<Result> result;

    private final ResultRepository repository;

    @Inject
    public ResultViewModel(ResultRepository repository){
        this.repository = repository;

        result = new MutableLiveData<>();
    }

    public MutableLiveData<Result> getResult() {
        return result;
    }

    public void add(Result result){
        repository.add(result);
    }
}

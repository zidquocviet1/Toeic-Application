package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.repository.ExamRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ExamViewModel extends ViewModel {
    private final MutableLiveData<List<Question>> questions;

    private final ExamRepository repository;

    @Inject
    public ExamViewModel(ExamRepository repository){
        questions = new MutableLiveData<>();

        this.repository = repository;
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }

    public void getListQuestionByCourseID(Long courseID){
        repository.getListQuestionByCourseID(this.questions, courseID);
    }
}

package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.Progress;
import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.repository.ExamRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ExamViewModel extends ViewModel {
    private final MutableLiveData<List<Question>> questions;
    private final MutableLiveData<Progress> progress;
    private final MutableLiveData<Map<Integer, String>> selectedQuestion;

    private final ExamRepository repository;

    @Inject
    public ExamViewModel(ExamRepository repository){
        questions = new MutableLiveData<>();
        selectedQuestion = new MutableLiveData<>();
        progress = new MutableLiveData<>();

        this.repository = repository;
        selectedQuestion.setValue(new HashMap<>());
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }

    public MutableLiveData<Progress> getProgress() {
        return progress;
    }

    public MutableLiveData<Map<Integer, String>> getSelectedQuestion() {
        return selectedQuestion;
    }

    public void postSelectedQuestion(int numQuestion, String answer){
        Map<Integer, String> selected = this.getSelectedQuestion().getValue();

        if (selected == null) selected  = new HashMap<>();

        selected.put(numQuestion, answer);
        this.getSelectedQuestion().postValue(selected);
    }

    public void getListQuestionByCourseID(Long courseID){
        repository.getListQuestionByCourseID(this.questions, courseID);
    }

    public void getProgressByCourseID(Long courseID){
        repository.getProgressByCourseID(this.progress, courseID);
    }
}

package com.example.toeicapplication.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.model.Progress;
import com.example.toeicapplication.model.Question;

import java.util.List;

public interface ExamRepository {
    void getListQuestionByCourseID(MutableLiveData<List<Question>> request, Long courseID);

    void getProgressByCourseID(MutableLiveData<Progress> request, Long courseID);
}

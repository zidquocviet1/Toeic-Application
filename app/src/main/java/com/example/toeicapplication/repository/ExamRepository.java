package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.Progress;
import com.example.toeicapplication.model.entity.Question;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface ExamRepository {
    Single<List<Question>> getListQuestionByCourseID(Long courseID);

    Single<Progress> getProgressByCourseID(Long courseID);

    Completable add(Progress progress);

    Completable delete(Long id);
}

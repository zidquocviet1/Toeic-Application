package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.Progress;
import com.example.toeicapplication.model.entity.Question;

import java.util.List;

import io.reactivex.Single;

public interface ExamRepository extends BaseRepository<Progress, Long>{
    Single<List<Question>> getListQuestionByCourseID(Long courseID);

    Single<Progress> getProgressByCourseID(Long courseID);
}

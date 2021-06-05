package com.example.toeicapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.toeicapplication.model.Question;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface QuestionDAO {
    @Insert
    void insertAll(List<Question> questions);

    @Query("select * from question where courseID = :courseID")
    Single<List<Question>> getQuestionById(Long courseID);
}

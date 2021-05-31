package com.example.toeicapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.toeicapplication.db.model.Word;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface WordDAO {
    @Query("select * from word limit 30")
    Single<List<Word>> getTop30Words();

    @Query("select * from word")
    Single<List<Word>> getAllWords();

    @Insert
    Completable insertAll(List<Word> words);
}

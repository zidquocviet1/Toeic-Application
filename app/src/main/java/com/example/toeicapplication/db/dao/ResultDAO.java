package com.example.toeicapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.toeicapplication.model.Result;

import io.reactivex.Completable;

@Dao
public interface ResultDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable add(Result result);
}

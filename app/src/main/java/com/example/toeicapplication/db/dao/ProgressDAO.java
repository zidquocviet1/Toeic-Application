package com.example.toeicapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.toeicapplication.model.Progress;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ProgressDAO {
    @Query("select * from Progress where courseID = :id")
    Single<Progress> getByCourseID(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable add(Progress progress);
}

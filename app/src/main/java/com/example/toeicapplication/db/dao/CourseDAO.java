package com.example.toeicapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.toeicapplication.db.model.Course;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CourseDAO {
    @Query("select * from course")
    Single<List<Course>> getAllCourses();

    @Insert
    void insertAll(Course... courses);
}

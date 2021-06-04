package com.example.toeicapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.toeicapplication.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface UserDAO {
    @Query("select * from user_info where id = :id")
    Single<User> getUserById(Long id);

    @Query("select * from user_info")
    Flowable<List<User>> getAllUsers();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    // completable doesn't care the return value
    Completable updateUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addUser(User user);

    @Query("select * from user_info order by timestamp desc limit 1")
    Single<User> recentLogoutUser();
}

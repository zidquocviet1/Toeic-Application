package com.example.toeicapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.toeicapplication.model.entity.RemoteUser;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.relations.RemoteUserWithResults;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public abstract class RankDAO {
    @Transaction
    @Query("select * from remote_user")
    // in the relationship we must to create a query in the parent table
    public abstract Flowable<List<RemoteUserWithResults>> remoteUserWithResults();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void addRemoteUser(RemoteUser user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertResult(List<Result> results);

    @Transaction
    public void addRemoteUserWithResults(List<User> users){
        for (User user : users){
            addRemoteUser(new RemoteUser(user.getId(), user.getUserName(), user.getDisplayName(), user.getTimestamp()));
            insertResult(user.getResults());
        }
    }
}

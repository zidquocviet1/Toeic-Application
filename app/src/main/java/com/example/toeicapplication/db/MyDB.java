package com.example.toeicapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.toeicapplication.db.dao.UserDAO;
import com.example.toeicapplication.db.model.User;

@Database(entities = {User.class}, exportSchema = false, version = 2)
public abstract class MyDB extends RoomDatabase {
    private static MyDB instance;

    public static synchronized MyDB getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static MyDB create(final Context context) {
        return Room.databaseBuilder(context, MyDB.class, "toeic_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public MyDB(){ }

    public abstract UserDAO getUserDAO();
}

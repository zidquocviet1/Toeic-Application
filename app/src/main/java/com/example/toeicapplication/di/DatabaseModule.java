package com.example.toeicapplication.di;

import android.content.Context;

import androidx.room.RoomDatabase;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.db.dao.UserDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Singleton
    @Provides
    MyDB provideDatabase(@ApplicationContext Context context){
        return MyDB.getInstance(context);
    }

    @Singleton
    @Provides
    UserDAO provideUserDAO(MyDB database){ return database.getUserDAO(); }
}

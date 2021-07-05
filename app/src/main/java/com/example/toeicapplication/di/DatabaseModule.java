package com.example.toeicapplication.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.db.dao.ResultDAO;
import com.example.toeicapplication.db.dao.UserDAO;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.workers.SeedDatabaseWorker;

import org.jetbrains.annotations.NotNull;

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
        return Room.databaseBuilder(context, MyDB.class, AppConstants.DB_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        WorkRequest request = OneTimeWorkRequest.from(SeedDatabaseWorker.class);
                        WorkManager.getInstance(context).enqueue(request);
                    }
                })
                .build();
    }

    @Singleton
    @Provides
    UserDAO provideUserDAO(MyDB database){ return database.getUserDAO(); }

    @Singleton
    @Provides
    ResultDAO provideResultDAO(MyDB database){
        return database.getResultDAO();
    }
}

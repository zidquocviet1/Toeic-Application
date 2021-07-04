package com.example.toeicapplication.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.toeicapplication.db.converter.CommentArrayConverter;
import com.example.toeicapplication.db.converter.LocalDateTimeConverter;
import com.example.toeicapplication.db.converter.ResultArrayConverter;
import com.example.toeicapplication.db.dao.CourseDAO;
import com.example.toeicapplication.db.dao.ProgressDAO;
import com.example.toeicapplication.db.dao.QuestionDAO;
import com.example.toeicapplication.db.dao.RankDAO;
import com.example.toeicapplication.db.dao.ResultDAO;
import com.example.toeicapplication.db.dao.UserDAO;
import com.example.toeicapplication.db.dao.WordDAO;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Progress;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.model.entity.Rank;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.workers.SeedDatabaseWorker;

import org.jetbrains.annotations.NotNull;

@Database(entities = {User.class, Result.class,
        Course.class, Rank.class,
        Word.class, Question.class,
        Progress.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class, CommentArrayConverter.class, ResultArrayConverter.class})
public abstract class MyDB extends RoomDatabase {
    private static MyDB instance;

    public static synchronized MyDB getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static MyDB create(final Context context) {
        return Room.databaseBuilder(context, MyDB.class, "toeic_database")
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        WorkRequest request = OneTimeWorkRequest.from(SeedDatabaseWorker.class);
                        WorkManager.getInstance(context).enqueue(request);
                    }
                })
                .build();
    }

    public MyDB() {
    }

    public abstract UserDAO getUserDAO();

    public abstract CourseDAO getCourseDAO();

    public abstract WordDAO getWordDAO();

    public abstract QuestionDAO getQuestionDAO();

    public abstract ProgressDAO getProgressDAO();

    public abstract ResultDAO getResultDAO();

    public abstract RankDAO getRankDAO();
}

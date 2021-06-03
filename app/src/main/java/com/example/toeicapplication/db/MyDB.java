package com.example.toeicapplication.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.toeicapplication.db.dao.CourseDAO;
import com.example.toeicapplication.db.dao.UserDAO;
import com.example.toeicapplication.db.dao.WordDAO;
import com.example.toeicapplication.db.model.Course;
import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.db.model.Word;
import com.example.toeicapplication.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Database(entities = {User.class, Course.class, Word.class}, exportSchema = false, version = 2)
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
                        Executors
                                .newSingleThreadScheduledExecutor()
                                .execute(() -> {
                                    getInstance(context).getCourseDAO().insertAll(Course.courses);
                                    List<Word> words = getWordFromAssets(context);

                                    getInstance(context).getWordDAO().insertAll(words);
                                });
                    }
                })
                .build();
    }

    private static List<Word> getWordFromAssets(Context context){
        try {
            InputStream is = context.getAssets().open("vocab_json.json");
            String jsonFileString = Utils.getJsonFromAssets(is);

            Gson gson = new Gson();
            Type listWordType = new TypeToken<List<Word>>() {}.getType();

            return gson.fromJson(jsonFileString, listWordType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    private static List<Word> getQuestionFromAssets(Context context){
//        try {
//            InputStream is = context.getAssets().open("question.json");
//            String jsonFileString = Utils.getJsonFromAssets(is);
//
//            Gson gson = new Gson();
//            Type listQuestionType = new TypeToken<List<Question>>() {}.getType();
//            List<Question> questions = gson.fromJson(jsonFileString, listQuestionType);
//
//            questions.forEach(q -> {
//                q.setAudioFile("course_" + q.getCourseID() + "/" + q.getAudioFile());
//                questionDAO().insertAll(q);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public MyDB() { }

    public abstract UserDAO getUserDAO();

    public abstract CourseDAO getCourseDAO();

    public abstract WordDAO getWordDAO();
}

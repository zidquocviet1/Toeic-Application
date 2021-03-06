package com.example.toeicapplication.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class SeedDatabaseWorker extends Worker {
    private final MyDB db;

    @AssistedInject
    public SeedDatabaseWorker(@Assisted @NonNull @NotNull Context context,
                              @Assisted @NonNull @NotNull WorkerParameters workerParams,
                              MyDB db) {
        super(context, workerParams);
        this.db = db;
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        try {
            Context context = getApplicationContext();

            List<Word> words = getWordFromAssets(context);
            List<Question> questions = getQuestionFromAssets(context);

            db.getCourseDAO().insertAll(Course.courses);
            db.getWordDAO().insertAll(words);
            db.getQuestionDAO().insertAll(questions);

            return Result.success();
        }catch (Exception e) {
            return Result.failure();
        }
    }

    private static List<Word> getWordFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open("vocab_json.json");
            String jsonFileString = Utils.getJsonFromAssets(is);

            Gson gson = new Gson();
            Type listWordType = new TypeToken<List<Word>>() {
            }.getType();

            return gson.fromJson(jsonFileString, listWordType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Question> getQuestionFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open("question.json");
            String jsonFileString = Utils.getJsonFromAssets(is);

            Gson gson = new Gson();
            Type listQuestionType = new TypeToken<List<Question>>() {
            }.getType();
            List<Question> questions = gson.fromJson(jsonFileString, listQuestionType);

            if (questions != null && !questions.isEmpty()) {
                return questions.stream()
                        .map(q -> {
                            q.setAudioFile("course_" + q.getCourseID() + "/" + q.getAudioFile());
                            return q;
                        })
                        .collect(Collectors.toList());
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

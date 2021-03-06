package com.example.toeicapplication.db;

 import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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
 import com.example.toeicapplication.model.entity.RemoteUser;
 import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;

@Database(entities = {User.class, Result.class, RemoteUser.class,
        Course.class, Rank.class,
        Word.class, Question.class,
        Progress.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class, CommentArrayConverter.class, ResultArrayConverter.class})
public abstract class MyDB extends RoomDatabase {
    public abstract UserDAO getUserDAO();

    public abstract CourseDAO getCourseDAO();

    public abstract WordDAO getWordDAO();

    public abstract QuestionDAO getQuestionDAO();

    public abstract ProgressDAO getProgressDAO();

    public abstract ResultDAO getResultDAO();

    public abstract RankDAO getRankDAO();
}

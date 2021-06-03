package com.example.toeicapplication.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.db.model.Course;
import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.db.model.Word;

import java.util.List;

public interface HomeRepository {
    void getAllUsers(MutableLiveData<List<User>> request);

    void addUser(User user);

    void updateUser(User newUser);

    void getAllCourses(MutableLiveData<List<Course>> request);

    void getAllWords(MutableLiveData<List<Word>> request);

    void get30Words(MutableLiveData<List<Word>> request);

    void updateLearnedWord(List<Word> words);

    void callRemoteUser(MutableLiveData<User> request, Long id);
}

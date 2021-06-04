package com.example.toeicapplication.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.model.Course;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.model.Word;
import com.example.toeicapplication.utilities.DataState;

import java.util.List;

public interface HomeRepository {
    void getAllUsers(MutableLiveData<List<User>> request);

    void addUser(User user);

    void updateUser(User newUser);

    void getAllCourses(MutableLiveData<List<Course>> request);

    void getAllWords(MutableLiveData<DataState<List<Word>>> request);

    void get30Words(MutableLiveData<List<Word>> request);

    void updateLearnedWord(List<Word> words);

    void callRemoteUser(MutableLiveData<DataState<User>> request, Long id);

    void callLogout(User user);

    void getRecentLogOutUser(MutableLiveData<User> request);
}

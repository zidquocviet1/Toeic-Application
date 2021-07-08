package com.example.toeicapplication.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.model.relations.RemoteUserWithResults;
import com.example.toeicapplication.utilities.Resource;

import java.util.List;

import io.reactivex.Observable;

public interface HomeRepository {
    void getAllUsers(MutableLiveData<List<User>> request);

    void addUser(User user);

    void updateUser(User newUser);

    void getAllCourses(MutableLiveData<List<Course>> request);

    void getAllWords(MutableLiveData<Resource<List<Word>>> request);

    void get30Words(MutableLiveData<List<Word>> request);

    void updateLearnedWord(List<Word> words);

    void callRemoteUser(MutableLiveData<Resource<User>> request, Long id);

    void callLogout(User user);

    void getRecentLogOutUser(MutableLiveData<User> request);

    Observable<List<RemoteUserWithResults>> getLeaderboard(Course course, boolean hasNetwork);
}

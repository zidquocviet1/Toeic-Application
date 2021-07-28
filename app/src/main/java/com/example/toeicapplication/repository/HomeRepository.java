package com.example.toeicapplication.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.model.relations.RemoteUserWithResults;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.utilities.Resource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface HomeRepository {
    void getAllUsers(MutableLiveData<List<User>> request);

    void addUser(User user);

    Completable updateUser(User newUser);

    void getAllCourses(MutableLiveData<List<Course>> request);

    void getAllWords(MutableLiveData<Resource<List<Word>>> request);

    void get30Words(MutableLiveData<List<Word>> request);

    void updateLearnedWord(List<Word> words);

    void callLogout(User user);

    void getRecentLogOutUser(MutableLiveData<User> request);

    Single<User> getLoginUser();

    Observable<List<RemoteUserWithResults>> getLeaderboard(Course course, boolean hasNetwork);

    Observable<User> loadUserFromLocalAndRemote(User user, boolean hasNetwork);

    Observable<MyResponse<List<Comment>>> getAllComment(Integer volume);
}

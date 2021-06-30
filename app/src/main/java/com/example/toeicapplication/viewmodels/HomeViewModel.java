package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.utilities.DataState;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<User>> users;
    private final MutableLiveData<List<Course>> courses;
    private final MutableLiveData<DataState<List<Word>>> words;
    private final MutableLiveData<List<Word>> top30Words;
    private final MutableLiveData<Boolean> networkState;

    private final MutableLiveData<User> cacheUser;
    private final MutableLiveData<User> recentLogOutUser;
    private final MutableLiveData<DataState<User>> remoteUser;

    private final HomeRepository repository;
    private final CompositeDisposable cd;

    @Inject
    public HomeViewModel(HomeRepository repository) {
        users = new MutableLiveData<>();
        courses = new MutableLiveData<>();
        cacheUser = new MutableLiveData<>();
        words = new MutableLiveData<>();
        top30Words = new MutableLiveData<>();
        remoteUser = new MutableLiveData<>();
        networkState = new MutableLiveData<>();
        recentLogOutUser = new MutableLiveData<>();

        this.repository = repository;
        cd = new CompositeDisposable();
        networkState.setValue(false);
    }

    public MutableLiveData<Boolean> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public MutableLiveData<List<Word>> getTop30Words() {
        return top30Words;
    }

    public MutableLiveData<DataState<List<Word>>> getWords() {
        return words;
    }

    public MutableLiveData<List<Course>> getCourses() {
        return courses;
    }

    public MutableLiveData<User> getCacheUser() {
        return cacheUser;
    }

    public MutableLiveData<User> getRecentLogOutUserLiveData() {
        return recentLogOutUser;
    }

    public MutableLiveData<DataState<User>> getRemoteUser() {
        return remoteUser;
    }

    public void setUserList(List<User> users) {
        this.users.setValue(users);
    }

    public void setUser(User user) {
        List<User> newList = this.users.getValue();
        newList.add(user);

        // setValue use in main thread, postValue use in background thread
        this.users.setValue(newList);
    }

    public void addUser(User user) {
        repository.addUser(user);
    }

    public void getAllUsers() {
        repository.getAllUsers(this.getUsers());
    }

    public void callRemoteUser(Long id){
        repository.callRemoteUser(this.remoteUser, id);
    }

    public void updateUser(User newUser) {
        repository.updateUser(newUser);
    }

    public void getAllCourses(){
        repository.getAllCourses(this.courses);
    }

    public void getAllWords(){
        repository.getAllWords(this.words);
    }

    public void get30Words(){
        repository.get30Words(this.top30Words);
    }

    public void updateLearnedWord(List<Word> words){
        repository.updateLearnedWord(words);
    }

    public void callLogout(User user){
        repository.callLogout(user);
    }

    public void getRecentLogOutUser(){
        repository.getRecentLogOutUser(this.recentLogOutUser);
    }

    // RankFragment
    public void getRankByCourse(Course course){
        repository.getRankByCourse(course);
    }
}

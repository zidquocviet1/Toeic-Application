package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.db.model.Course;
import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.db.model.Word;
import com.example.toeicapplication.repository.HomeRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<User>> users;
    private final MutableLiveData<List<Course>> courses;
    private final MutableLiveData<List<Word>> words;
    private final MutableLiveData<List<Word>> top30Words;
    private final MutableLiveData<User> onlineUser;
    private final HomeRepository repository;

    @Inject
    public HomeViewModel(HomeRepository repository) {
        users = new MutableLiveData<>();
        courses = new MutableLiveData<>();
        onlineUser = new MutableLiveData<>();
        words = new MutableLiveData<>();
        top30Words = new MutableLiveData<>();

        this.repository = repository;
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public MutableLiveData<List<Word>> getTop30Words() {
        return top30Words;
    }

    public MutableLiveData<List<Word>> getWords() {
        return words;
    }

    public MutableLiveData<List<Course>> getCourses() {
        return courses;
    }

    public MutableLiveData<User> getOnlineUser() {
        return onlineUser;
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
}

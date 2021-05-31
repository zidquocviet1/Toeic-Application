package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.repository.HomeRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<User>> users;
    private final HomeRepository repository;

    @Inject
    public HomeViewModel(HomeRepository repository) {
        users = new MutableLiveData<>();
        this.repository = repository;

        users.setValue(null);
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
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
}

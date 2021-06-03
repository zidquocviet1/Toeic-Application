package com.example.toeicapplication.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.db.dao.UserDAO;
import com.example.toeicapplication.db.model.Course;
import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.db.model.Word;
import com.example.toeicapplication.network.response.GetResponse;
import com.example.toeicapplication.network.service.UserService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeRepositoryImpl implements HomeRepository {
    private final CompositeDisposable compositeDisposable;
    private final UserService userService;
    private final Context context;
    private final MyDB database;

    @Inject
    public HomeRepositoryImpl(UserService userService,
                              MyDB database,
                              CompositeDisposable compositeDisposable,
                              @ApplicationContext Context context) {
        this.compositeDisposable = compositeDisposable;
        this.userService = userService;
        this.context = context;
        this.database = database;
    }

    @Override
    public void getAllUsers(MutableLiveData<List<User>> request) {
        compositeDisposable.add(
                database.getUserDAO().getAllUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(users -> {
                            request.postValue(users);
                            Log.e("TAG", "Lay du lieu User tu database thanh cong");
                        }, throwable -> {
                            Log.e("TAG", "Lay du lieu User tu database that bai: " + throwable.getMessage());
                            request.postValue(null);
                            throwable.printStackTrace();
                        })
        );
    }

    @Override
    public void addUser(User user) {
        compositeDisposable.add(
                database.getUserDAO().addUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            Log.d("TAG", "them user thanh cong voi id = " + aLong);
                        })
        );
    }

    @Override
    public void updateUser(User newUser) {
        compositeDisposable.add(
                database.getUserDAO().updateUser(newUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->
                                Toast.makeText(context, "You are logging out successfully!", Toast.LENGTH_SHORT).show())
        );
    }

    @Override
    public void getAllWords(MutableLiveData<List<Word>> request) {
        compositeDisposable.add(
                database.getWordDAO().getAllWords()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(request::postValue, throwable -> {
                            Log.e("TAG", "Khong the lay du lieu khoa hoc tu database: " + throwable.getMessage());
                            throwable.printStackTrace();
                        })
        );
    }

    @Override
    public void get30Words(MutableLiveData<List<Word>> request) {
        compositeDisposable.add(
                database.getWordDAO().getTop30Words()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(request::postValue, throwable -> {
                            Log.e("TAG", "Khong the lay du lieu khoa hoc tu database: " + throwable.getMessage());
                            throwable.printStackTrace();
                        })
        );
    }

    @Override
    public void updateLearnedWord(List<Word> words) {
        compositeDisposable.add(
                database.getWordDAO().updateLearnedWord(words)
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> Log.e("TAG", "update tu vung da hoc thanh cong"),
                                throwable -> {
                                    Log.e("TAG", "update tu vung da hoc that bai");
                                })
        );
    }

    @Override
    public void callRemoteUser(MutableLiveData<User> request, Long id) {
        compositeDisposable.add(
                userService.findUser(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(5, TimeUnit.SECONDS)
                        .subscribe(userGetResponse -> {
                            if (userGetResponse.isStatus())
                                request.postValue(userGetResponse.getData().get(0));
                            Log.e("TAG", "Lay thong tin nguoi dung tu remote thanh cong");
                        }, throwable -> {
                            Log.e("TAG", "Lay thong tin nguoi dung tu remote that bai");
                        })
        );
    }

    @Override
    public void getAllCourses(MutableLiveData<List<Course>> request) {
        compositeDisposable.add(
                database.getCourseDAO().getAllCourses()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(request::postValue, throwable -> {
                            Log.e("TAG", "Khong the lay du lieu khoa hoc tu database: " + throwable.getMessage());
                            throwable.printStackTrace();
                        })
        );
    }
}

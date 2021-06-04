package com.example.toeicapplication.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.R;
import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.model.Course;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.model.Word;
import com.example.toeicapplication.network.response.Response;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.utilities.DataState;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
                            Log.e("TAG", "Get User data from db successful");
                        }, throwable -> {
                            Log.e("TAG", "Get User data from db failure: " + throwable.getMessage());
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
                        .subscribe(aLong -> Log.d("TAG", "Add user with id = " + aLong))
        );
    }

    @Override
    public void updateUser(User newUser) {
        compositeDisposable.add(
                database.getUserDAO().updateUser(newUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->
                                Toast.makeText(context, "You are logging out successfully!",
                                        Toast.LENGTH_SHORT).show())
        );
    }

    @Override
    public void getAllWords(MutableLiveData<DataState<List<Word>>> request) {
        request.postValue(DataState.Loading(null));

        compositeDisposable.add(
                database.getWordDAO().getAllWords()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(words -> {
                            if (words != null){
                                request.postValue(DataState.Success(words));
                            }
                        }, throwable -> {
                            request.postValue(DataState.Error(throwable.getMessage()));
                            Log.e("TAG", "Can't get the course data from database: "
                                    + throwable.getMessage());
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
                            Log.e("TAG", "Can't get the top 30 words from database: "
                                    + throwable.getMessage());
                            throwable.printStackTrace();
                        })
        );
    }

    @Override
    public void updateLearnedWord(List<Word> words) {
        compositeDisposable.add(
                database.getWordDAO().updateLearnedWord(words)
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> Log.e("TAG", "Update learned words successful"),
                                throwable -> Log.e("TAG", "Update learned words failure"))
        );
    }

    @Override
    public void callRemoteUser(MutableLiveData<DataState<User>> request, Long id) {
        request.postValue(DataState.Loading(null));

        compositeDisposable.add(
                userService.findUser(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(5, TimeUnit.SECONDS)
                        .subscribe(userGetResponse -> {
                            if (userGetResponse.isStatus()) {
                                User user = userGetResponse.getData().get(0);

                                request.postValue(DataState.Success(user));
                            } else {
                                request.postValue(DataState.Error(userGetResponse.getMessage()));
                            }
                        }, throwable -> request.postValue(DataState.Error(context.getString(R.string.server_error))))
        );
    }

    @Override
    public void getAllCourses(MutableLiveData<List<Course>> request) {
        compositeDisposable.add(
                database.getCourseDAO().getAllCourses()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(request::postValue, throwable -> {
                            Log.e("TAG", "Get the course data failure: " + throwable.getMessage());
                            throwable.printStackTrace();
                        })
        );
    }

    @Override
    public void callLogout(User user) {
        compositeDisposable.add(
                userService.logout(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(userPutResponse -> {
                            if (userPutResponse.isStatus()) {
                                Log.e("TAG", "Log out user successfully with id = " + user.getId());
                            }
                        }, throwable -> {
                            Log.e("TAG", "Log out failure: " + throwable.getMessage());
                            throwable.printStackTrace();
                        })
        );
    }

    @Override
    public void getRecentLogOutUser(MutableLiveData<User> request) {
        compositeDisposable.add(
                database.getUserDAO().recentLogoutUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            if (user != null) request.setValue(user);
                            Log.e("TAG", "Get recent log out user successfully");
                        }, throwable -> {
                            Log.e("TAG", "No user expected: " + throwable.getMessage());
                            throwable.printStackTrace();
                        })
        );
    }
}

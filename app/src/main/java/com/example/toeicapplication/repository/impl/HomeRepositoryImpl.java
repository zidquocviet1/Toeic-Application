package com.example.toeicapplication.repository.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.toeicapplication.R;
import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Rank;
import com.example.toeicapplication.model.entity.RemoteUser;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.relations.RemoteUserWithResults;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.NetworkBoundResource;
import com.example.toeicapplication.utilities.RateLimiter;
import com.example.toeicapplication.utilities.Resource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

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
                            request.postValue(
                                    users.stream()
                                        .filter(u -> u != null && u.isLogin() && !u.getPassword().equals(""))
                                        .collect(Collectors.toCollection(ArrayList::new)));
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
    public void getAllWords(MutableLiveData<Resource<List<Word>>> request) {
        request.postValue(Resource.Loading(null));

        compositeDisposable.add(
                database.getWordDAO().getAllWords()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(words -> {
                            if (words != null){
                                request.postValue(Resource.Success(words));
                            }
                        }, throwable -> {
                            request.postValue(Resource.Error(throwable.getMessage()));
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

    // not perfect case
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
    public void callRemoteUser(MutableLiveData<Resource<User>> request, Long id) {
        compositeDisposable.add(
                userService.findUser(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(5, TimeUnit.SECONDS)
                        .subscribeWith(new DisposableObserver<MyResponse<User>>() {
                            @Override
                            public void onNext(@NotNull MyResponse<User> userResponse) {
                                if (userResponse.isStatus()) {
                                    User user = userResponse.getData();

                                    request.postValue(Resource.Success(user));
                                } else {
                                    request.postValue(Resource.Error(userResponse.getMessage()));
                                }
                            }

                            @Override
                            public void onError(@NotNull Throwable e) {
                                request.postValue(Resource.Error(context.getString(R.string.server_error)));
                            }

                            @Override
                            public void onComplete() {

                            }
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

    @Override
    public Observable<List<RemoteUserWithResults>> getLeaderboard(Course course, boolean hasNetwork){
        RateLimiter<Long> rateLimiter = new RateLimiter<>(10, TimeUnit.MINUTES);
        Long courseId = course == null ? 999999L : course.getId();

        return new NetworkBoundResource<List<RemoteUserWithResults>, Response<MyResponse<List<User>>>>(){
            @Override
            protected void saveCallResult(Response<MyResponse<List<User>>> item) {
                if (item.code() == HttpsURLConnection.HTTP_OK){
                    MyResponse<List<User>> body = item.body();
                    if (body != null){
                        List<User> data = body.getData();

                        if (data != null && !data.isEmpty() && body.isStatus()) {
                            database.getRankDAO().addRemoteUserWithResults(data);
                        }
                    }
                }else if (item.code() == HttpsURLConnection.HTTP_NOT_FOUND){
                    Log.d(AppConstants.TAG, "The course has no leaderboard");
                }
            }

            @Override
            protected boolean shouldFetch(List<RemoteUserWithResults> data) {
                return hasNetwork && (data == null || data.isEmpty() || rateLimiter.shouldFetch(courseId));
            }

            @Override
            protected Flowable<List<RemoteUserWithResults>> loadFromDb() {
                return database.getRankDAO().remoteUserWithResults();
            }

            @Override
            protected Observable<Response<MyResponse<List<User>>>> createCall() {
                return userService.leaderboard("RANK", courseId);
            }

            @Override
            protected void onFetchFailed(Throwable throwable) {
                super.onFetchFailed(throwable);
                rateLimiter.reset(courseId);
            }
        }.asObservable();
    }
}

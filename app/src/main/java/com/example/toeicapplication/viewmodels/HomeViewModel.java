package com.example.toeicapplication.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.RankInfo;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.model.relations.RemoteUserWithResults;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.utilities.Resource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    // Home Page
    private final MutableLiveData<List<Course>> courses;
    private final MutableLiveData<List<Word>> top30Words;
    private final MutableLiveData<User> loginUserFromLocal;
    private final MutableLiveData<User> loginUser;

    // Vocabulary Fragment
    private final MutableLiveData<Resource<List<Word>>> words;

    private final MutableLiveData<Boolean> networkState;

    // Result Data from Login Activity
    private final MutableLiveData<User> recentLogOutUser;

    // Rank Fragment
    private final MutableLiveData<Resource<List<RankInfo>>> listRankInfo;

    // Dependencies
    private final HomeRepository repository;
    private final CompositeDisposable cd;

    @Inject
    public HomeViewModel(HomeRepository repository) {
        courses = new MutableLiveData<>();
        words = new MutableLiveData<>();
        top30Words = new MutableLiveData<>();
        networkState = new MutableLiveData<>();
        recentLogOutUser = new MutableLiveData<>();
        listRankInfo = new MutableLiveData<>();
        loginUserFromLocal = new MutableLiveData<>();
        loginUser = new MutableLiveData<>();
        cd = new CompositeDisposable();

        this.repository = repository;
        networkState.setValue(false);
        init();
    }

    private void init() {
        getLoginUser();
        getAllCourses();
        get30Words();
        getRecentLogOutUser();
    }

    // GETTER
    public MutableLiveData<Boolean> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<List<Word>> getTop30Words() {
        return top30Words;
    }

    public MutableLiveData<Resource<List<Word>>> getWords() {
        return words;
    }

    public MutableLiveData<List<Course>> getCourses() {
        return courses;
    }

    public MutableLiveData<User> getRecentLogOutUserLiveData() {
        return recentLogOutUser;
    }

    public LiveData<Resource<List<RankInfo>>> getListRankInfo() {
        return listRankInfo;
    }

    public LiveData<User> getLoginUserFromLocalLiveData() {
        return loginUserFromLocal;
    }

    public LiveData<User> getLoginUserLiveData() {
        return loginUser;
    }

    // communicate with Repository
    private void getAllCourses() {
        repository.getAllCourses(this.courses);
    }

    private void get30Words() {
        repository.get30Words(this.top30Words);
    }

    private void getRecentLogOutUser() {
        repository.getRecentLogOutUser(this.recentLogOutUser);
    }

    private void getLoginUser() {
        cd.add(repository.getLoginUser().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    if (user != null) loginUser.postValue(user);
                }, throwable -> loginUser.postValue(null)));
    }

    public void addUser(User user) {
        repository.addUser(user);
    }

    // use this method from Login Response
    public void updateUserFromLocal(@NonNull User user) {
        loginUserFromLocal.postValue(user);
    }

    public void loadUserFromLocalAndRemote(User user, boolean hasNetwork) {
        repository.loadUserFromLocalAndRemote(user, hasNetwork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(@NotNull User user) {
                        loginUserFromLocal.postValue(user);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        loginUserFromLocal.postValue(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateUser(User newUser, Context context) {
        cd.add(repository.updateUser(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    loginUserFromLocal.postValue(null);
                    Toast.makeText(context, "You are logging out successfully!",
                            Toast.LENGTH_SHORT).show();
                })
        );
    }

    public void getAllWords() {
        repository.getAllWords(this.words);
    }

    public void updateLearnedWord(List<Word> words) {
        repository.updateLearnedWord(words);
    }

    public void callLogout(User user) {
        repository.callLogout(user);
    }

    // RankFragment
    public void getLeaderboard(Course course, boolean hasNetwork) {
        repository.getLeaderboard(course, hasNetwork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RemoteUserWithResults>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        listRankInfo.postValue(Resource.Loading(null));
                    }

                    @Override
                    public void onNext(@NotNull List<RemoteUserWithResults> remoteUserWithResults) {
                        List<RankInfo> info = new ArrayList<>();

                        remoteUserWithResults.forEach(r -> {
                            List<Result> results = r.results;

                            if (results != null && !results.isEmpty()) {
                                if (course != null) {
                                    results.stream()
                                            .filter(item -> item.getCourseId().equals(course.getId()))
                                            .findFirst()
                                            .ifPresent(firstResult -> info.add(new RankInfo(r.remoteUser, firstResult)));
                                } else {
                                    results.stream()
                                            .max((o1, o2) -> o1.getScore().compareTo(o2.getScore()))
                                            .ifPresent(firstResult -> info.add(new RankInfo(r.remoteUser, firstResult)));
                                }
                            }
                        });

                        listRankInfo.postValue(Resource.Success(info));
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        listRankInfo.postValue(Resource.Error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

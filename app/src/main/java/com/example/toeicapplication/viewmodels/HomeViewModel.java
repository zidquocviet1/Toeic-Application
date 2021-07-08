package com.example.toeicapplication.viewmodels;

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
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    // Home Page
    private final MutableLiveData<List<User>> users;
    private final MutableLiveData<List<Course>> courses;
    private final MutableLiveData<List<Word>> top30Words;

    // Vocabulary Fragment
    private final MutableLiveData<Resource<List<Word>>> words;

    private final MutableLiveData<Boolean> networkState;

    // Result Data from Login Activity
    private final MutableLiveData<User> cacheUser;
    private final MutableLiveData<User> recentLogOutUser;
    private final MutableLiveData<Resource<User>> remoteUser;

    // Rank Fragment
    private final MutableLiveData<Resource<List<RankInfo>>> listRankInfo;

    // Dependencies
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
        listRankInfo = new MutableLiveData<>();
        cd = new CompositeDisposable();

        this.repository = repository;
        networkState.setValue(false);
        init();
    }

    private void init(){
        getAllUsers();
        getAllCourses();
        get30Words();
        getRecentLogOutUser();
    }

    // GETTER
    public MutableLiveData<Boolean> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
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

    public MutableLiveData<User> getCacheUser() {
        return cacheUser;
    }

    public MutableLiveData<User> getRecentLogOutUserLiveData() {
        return recentLogOutUser;
    }

    public MutableLiveData<Resource<User>> getRemoteUser() {
        return remoteUser;
    }

    public LiveData<Resource<List<RankInfo>>> getListRankInfo() {
        return listRankInfo;
    }

    // communicate with Repository
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
    public void getLeaderboard(Course course, boolean hasNetwork){
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
                                }else{
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

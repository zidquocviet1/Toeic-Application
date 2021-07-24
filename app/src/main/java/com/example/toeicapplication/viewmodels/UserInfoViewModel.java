package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class UserInfoViewModel extends ViewModel {
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<List<Course>> courseListLiveData;
    private final MutableLiveData<List<Result>> resultListLiveData;
    private final MutableLiveData<Map<Course, List<Result>>> courseWithResultLiveData;
    private final UserInfoRepository repository;
    private final CompositeDisposable cd;

    @Inject
    public UserInfoViewModel(UserInfoRepository repository) {
        userLiveData = new MutableLiveData<>();
        courseListLiveData = new MutableLiveData<>();
        resultListLiveData = new MutableLiveData<>();
        courseWithResultLiveData = new MutableLiveData<>();

        this.repository = repository;
        this.cd = new CompositeDisposable();

        getCourseWithResults();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<List<Course>> getCourseListLiveData() {
        return courseListLiveData;
    }

    public LiveData<List<Result>> getResultListLiveData() {
        return resultListLiveData;
    }

    public LiveData<Map<Course, List<Result>>> getCourseWithResultLiveData() {
        return courseWithResultLiveData;
    }

    public void setUser(User user){
        userLiveData.setValue(user);
    }

    private void getCourseWithResults() {
        cd.add(repository.getCourseWithResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courseWithResults -> {
                    List<Course> courses = new ArrayList<>();
                    Map<Course, List<Result>> mapper = new HashMap<>();
                    User user = userLiveData.getValue();

                    if (courseWithResults != null && !courseWithResults.isEmpty()) {
                        courseWithResults.forEach(c -> {
                            courses.add(c.course);
                            mapper.put(c.course, c.results
                                    .stream()
                                    .filter(r -> r.getUserId().equals(user.getId()))
                            .collect(Collectors.toList()));
                        });
                    }

                    courseListLiveData.postValue(courses);
                    courseWithResultLiveData.postValue(mapper);
                }, Throwable::printStackTrace));
    }

    @Override
    protected void onCleared() {
        if (cd != null && !cd.isDisposed()) cd.dispose();
        super.onCleared();
    }
}

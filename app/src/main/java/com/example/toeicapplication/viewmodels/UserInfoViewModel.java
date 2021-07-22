package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.repository.UserInfoRepository;

import java.util.List;

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
    private final UserInfoRepository repository;
    private final CompositeDisposable cd;

    @Inject
    public UserInfoViewModel(UserInfoRepository repository) {
        userLiveData = new MutableLiveData<>();
        courseListLiveData = new MutableLiveData<>();
        resultListLiveData = new MutableLiveData<>();

        this.repository = repository;
        this.cd = new CompositeDisposable();

        getAllCourses();
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

    private void getAllCourses() {
        cd.add(repository.getAllCourses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courses -> {
                    if (courses != null && !courses.isEmpty())
                        courseListLiveData.postValue(courses);
                }, Throwable::printStackTrace));
    }
}

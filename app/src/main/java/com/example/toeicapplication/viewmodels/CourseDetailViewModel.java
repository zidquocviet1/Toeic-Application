package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.model.CourseMapper;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.repository.CourseDetailRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class CourseDetailViewModel extends ViewModel {
    private final MutableLiveData<List<CourseMapper>> courseInfoLiveData;
    private final MutableLiveData<List<Comment>> commentLiveData;

    private final CourseDetailRepository repository;
    private final CompositeDisposable cd;

    @Inject
    public CourseDetailViewModel(CourseDetailRepository repository) {
        courseInfoLiveData = new MutableLiveData<>();
        commentLiveData = new MutableLiveData<>();
        this.repository = repository;
        this.cd = new CompositeDisposable();
    }

    public LiveData<List<CourseMapper>> getCourseInfoLiveData() {
        return courseInfoLiveData;
    }

    public LiveData<List<Comment>> getCommentLiveData() {
        return commentLiveData;
    }

    /*
     * this method call to the server to get all the users have toke a test in context course
     * */
    public void getCourseInfo(Long courseId) {
        cd.add(repository.getCourseInfo(courseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CourseMapper>>() {
                    @Override
                    public void onNext(@NotNull List<CourseMapper> courseMappers) {
                        if (!courseMappers.isEmpty()) {
                            courseInfoLiveData.postValue(courseMappers);
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        courseInfoLiveData.postValue(null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    public void getTopCommentByCourse(Long courseId, Long volume) {
        cd.add(repository.getTopCommentByCourse(courseId, volume)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listMyResponse -> {
                    if (listMyResponse != null && listMyResponse.isStatus()) {
                        commentLiveData.postValue(listMyResponse.getData());
                    }
                }, Throwable::printStackTrace));
    }

    @Override
    protected void onCleared() {
        if (!cd.isDisposed()) cd.dispose();
        super.onCleared();
    }
}

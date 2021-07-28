package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.repository.CommentRepository;
import com.example.toeicapplication.utilities.Resource;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class CommentViewModel extends ViewModel {
    private final MutableLiveData<Resource<Comment>> commentLiveData;

    private final CommentRepository repository;
    private final CompositeDisposable cd;

    @Inject
    public CommentViewModel(CommentRepository repository) {
        commentLiveData = new MutableLiveData<>();
        this.repository = repository;
        this.cd = new CompositeDisposable();
    }

    public LiveData<Resource<Comment>> getCommentLiveData() {
        return commentLiveData;
    }

    public void postComment(@NotNull Comment comment) {
        commentLiveData.postValue(Resource.Loading(null));

        cd.add(repository
                .postComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commentMyResponse -> {
                    if (commentMyResponse != null && commentMyResponse.isStatus()) {
                        commentLiveData.postValue(Resource.Success(commentMyResponse.getData()));
                    }
                }, throwable -> commentLiveData.postValue(Resource.Error(throwable.getMessage())))
        );
    }
}

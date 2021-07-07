package com.example.toeicapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.repository.ResultRepository;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@HiltViewModel
public class ResultViewModel extends ViewModel {
    private final MutableLiveData<Result> result;
    private final CompositeDisposable cd;

    private final ResultRepository repository;

    @Inject
    public ResultViewModel(ResultRepository repository) {
        this.repository = repository;

        result = new MutableLiveData<>();
        cd = new CompositeDisposable();
    }

    public MutableLiveData<Result> getResult() {
        return result;
    }

    public void add(Result result) {
        Disposable addResultToDatabase = repository
                .add(result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.e("TAG", "Insert result successfully!"));

        Disposable addResultRemote = repository
                .addResultRemote(result)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Response<MyResponse<Result>>>() {
                    @Override
                    public void onNext(@NotNull Response<MyResponse<Result>> response) {
                        MyResponse<Result> body = response.body();

                        if (body != null) {
                            Log.e("TAG", body.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e("TAG", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        cd.add(addResultToDatabase);
        cd.add(addResultRemote);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
//        cd.clear(); // this is not accept new observable
    }
}

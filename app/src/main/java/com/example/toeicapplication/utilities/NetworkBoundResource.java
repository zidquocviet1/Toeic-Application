package com.example.toeicapplication.utilities;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

// The recommended network API from Google
// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
public abstract class NetworkBoundResource<ResultType, RequestType> {
    private Flowable<ResultType> result;

    @MainThread
    public NetworkBoundResource(){
        result = loadFromDb();

        result.subscribeOn(Schedulers.io())
                .doOnNext(resultType -> {
                    if (shouldFetch(resultType)){
                        fetchFromNetwork();
                    }else{
                        result = loadFromDb().subscribeOn(Schedulers.io());
                    }
                })
                .doOnError(this::onFetchFailed);
    }

    private void fetchFromNetwork(){
    }



    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract void saveCallResult(RequestType item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(ResultType data);

    // Called to get the cached data from the database.
    @MainThread
    protected abstract Flowable<ResultType> loadFromDb();

    // Called to create the API call.
    @MainThread
    protected abstract Observable<RequestType> createCall();

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected void onFetchFailed(Throwable throwable) {}

    public Observable<ResultType> asObservable(){
        return result.toObservable();
    }
}

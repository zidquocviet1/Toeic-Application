package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.repository.UserInfoRepository;
import com.example.toeicapplication.utilities.Resource;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@HiltViewModel
public class UserInfoViewModel extends ViewModel {
    private final MutableLiveData<Resource<User>> userLiveData;
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
    }

    public LiveData<Resource<User>> getUserLiveData() {
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

    public void findUserRemote(Long userId) {
        userLiveData.postValue(Resource.Loading(null));

        cd.add(repository.findUserRemote(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<MyResponse<User>>>() {
                    @Override
                    public void onNext(@NotNull Response<MyResponse<User>> response) {
                        MyResponse<User> body = response.body();
                        if (response.code() == HttpsURLConnection.HTTP_ACCEPTED){
                            if (body != null && body.isStatus()){
                                User user = body.getData();
                                userLiveData.postValue(Resource.Success(user));
                                if (user.getResults() != null && !user.getResults().isEmpty())
                                    resultListLiveData.postValue(user.getResults());
                            }
                        }else{
                            if (body != null) userLiveData.postValue(Resource.Error(body.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        userLiveData.postValue(Resource.Error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    protected void onCleared() {
        if (!cd.isDisposed()) cd.dispose();
        super.onCleared();
    }
}

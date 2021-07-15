package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.repository.EditProfileRepository;
import com.example.toeicapplication.utilities.Resource;
import com.example.toeicapplication.utilities.Status;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

@HiltViewModel
public class EditProfileViewModel extends ViewModel {
    private final EditProfileRepository repository;
    private final MutableLiveData<Resource<User>> responseUser;
    private final CompositeDisposable cd;

    private static final String AVATAR = "avatar";
    private static final String COVER = "cover";

    @Inject
    public EditProfileViewModel(EditProfileRepository repository) {
        this.repository = repository;

        responseUser = new MutableLiveData<>();
        cd = new CompositeDisposable();
    }

    public LiveData<Resource<User>> getResponseUser(){ return responseUser;}

    public void editProfile(String userId, String fullName, String bio, LocalDateTime birthday,
                             String address, String avatarPath) {
        File avatarFile = new File(avatarPath);

        String birthdayString = birthday == null ? "" : birthday.toString();

        RequestBody avatarRequestFile = MultipartBody.create(avatarFile, MediaType.parse("multipart/form-data"));
        RequestBody userIdBody = createFromString(userId);
        RequestBody fullNameBody = createFromString(fullName);
        RequestBody bioBody = createFromString(bio);
        RequestBody birthdayBody = createFromString(birthdayString);
        RequestBody addressBody = createFromString(address);

        MultipartBody.Part avatarBody = MultipartBody.Part.createFormData(AVATAR, avatarFile.getName(), avatarRequestFile);

        responseUser.postValue(Resource.Loading(null));
        cd.add(repository.editProfile(avatarBody, userIdBody, fullNameBody, bioBody, birthdayBody, addressBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<MyResponse<User>>>() {
                    @Override
                    public void onNext(@NotNull Response<MyResponse<User>> response) {
                        if (response.code() == HttpURLConnection.HTTP_ACCEPTED){
                            MyResponse<User> body = response.body();
                            if (body != null && body.isStatus()){
                                responseUser.postValue(Resource.Success(body.getData()));
                            }
                        }else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                            MyResponse<User> body = response.body();
                            if (body != null) {
                                responseUser.postValue(Resource.Error(body.getMessage()));
                            }
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        responseUser.postValue(Resource.Error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private RequestBody createFromString(String input) {
        return RequestBody.create(input, MediaType.parse("multipart/form-data"));
    }
}

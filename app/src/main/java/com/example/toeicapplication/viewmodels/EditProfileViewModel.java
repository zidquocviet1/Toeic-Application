package com.example.toeicapplication.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.repository.EditProfileRepository;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.Resource;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
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

    public LiveData<Resource<User>> getResponseUser() {
        return responseUser;
    }


    public void editProfile(Long userId, String fullName, String bio, LocalDateTime birthday,
                            String address, String avatarPath) {

        Observable<Response<MyResponse<User>>> observable;

        if (avatarPath.equals("")) {
            User user = new User();
            user.setId(userId);
            user.setDisplayName(fullName);
            user.setBiography(bio);
            user.setBirthday(birthday);
            user.setAddress(address);

            observable = repository.editProfile(user);
        } else {
            RequestBody userIdBody = createFromString(String.valueOf(userId));
            RequestBody fullNameBody = createFromString(fullName);
            RequestBody bioBody = createFromString(bio);
            RequestBody birthdayBody = createFromString(birthday == null ? "" : birthday.toString());
            RequestBody addressBody = createFromString(address);

            File avatarFile = new File(avatarPath);
            RequestBody avatarRequestFile = MultipartBody.create(avatarFile, MediaType.parse("multipart/form-data"));
            MultipartBody.Part avatarBody = MultipartBody.Part.createFormData(AVATAR, avatarFile.getName(), avatarRequestFile);

            observable = repository.editProfile(avatarBody, userIdBody, fullNameBody, bioBody, birthdayBody, addressBody);
        }

        responseUser.postValue(Resource.Loading(null));
        cd.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::onEditProfileResponse)
                .doOnError(this::onEditProfileError)
                .subscribe()
        );
    }

    private void onEditProfileResponse(Response<MyResponse<User>> response) {
        MyResponse<User> body = response.body();

        if (response.code() == HttpURLConnection.HTTP_ACCEPTED) {
            if (body != null && body.isStatus())
                responseUser.postValue(Resource.Success(body.getData()));
        } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            if (body != null) responseUser.postValue(Resource.Error(body.getMessage()));
        }
    }

    private void onEditProfileError(Throwable e) {
        responseUser.postValue(Resource.Error(e.getMessage()));
    }

    private RequestBody createFromString(String input) {
        return RequestBody.create(input, MediaType.parse("multipart/form-data"));
    }

    public void updateUserProfile(User user, Context context) {
        cd.add(repository.updateUserProfile(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(context, "Edit Profile successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e(AppConstants.TAG, "Update User Profile failure " + e.getMessage());
                    }
                })
        );
    }
}

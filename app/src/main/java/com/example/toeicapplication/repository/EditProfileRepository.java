package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public interface EditProfileRepository {
    /*
     * This call to edit the User Profile with avatar or cover any changes
     * */
    Observable<Response<MyResponse<User>>> editProfile(MultipartBody.Part avatarPart, RequestBody userId, RequestBody fullName,
                                                       RequestBody bio, RequestBody birthday, RequestBody address);

    /*
    * This call to edit the User Profile without avatar or cover any changes
    * The properties are included: Full Name, Biography, Birthday, Address
    * */
    Observable<Response<MyResponse<User>>> editProfile(User user);

    /*
    * Call this method to update the User Profile to the database after receiver User Response successfully
    * */
    Completable updateUserProfile(User user);
}

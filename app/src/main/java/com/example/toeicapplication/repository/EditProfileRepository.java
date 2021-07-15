package com.example.toeicapplication.repository;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public interface EditProfileRepository {
    Observable<Response<MyResponse<User>>> editProfile(MultipartBody.Part avatarPart, RequestBody userId, RequestBody fullName,
                                                       RequestBody bio, RequestBody birthday, RequestBody address);
}

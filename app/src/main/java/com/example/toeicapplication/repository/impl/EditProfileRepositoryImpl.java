package com.example.toeicapplication.repository.impl;

import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.network.response.MyResponse;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.EditProfileRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class EditProfileRepositoryImpl implements EditProfileRepository {
    private final UserService service;

    @Inject
    public EditProfileRepositoryImpl(UserService service){
        this.service = service;
    }

    @Override
    public Observable<Response<MyResponse<User>>> editProfile(MultipartBody.Part avatarPart, RequestBody userId,
                                                              RequestBody fullName, RequestBody bio,
                                                              RequestBody birthday, RequestBody address) {
        return service.editProfile(avatarPart, userId, fullName, bio, birthday, address);
    }
}

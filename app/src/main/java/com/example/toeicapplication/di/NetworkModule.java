package com.example.toeicapplication.di;

import com.example.toeicapplication.network.Api;
import com.example.toeicapplication.network.service.UserService;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Singleton
    @Provides
    public UserService provideUserService(){
        return Api.getInstance().userService();
    }

}

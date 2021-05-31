package com.example.toeicapplication.di;

import android.content.Context;

import com.example.toeicapplication.db.dao.UserDAO;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.repository.HomeRepositoryImpl;
import com.example.toeicapplication.repository.LoginRepository;
import com.example.toeicapplication.repository.LoginRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.disposables.CompositeDisposable;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Singleton
    @Provides
    public CompositeDisposable provideCD(){
        return new CompositeDisposable();
    }

    @Singleton
    @Provides
    public LoginRepository provideLoginRepository(UserService us, CompositeDisposable cd){
        return new LoginRepositoryImpl(us, cd);
    }

    @Singleton
    @Provides
    public HomeRepository provideHomeRepository(UserDAO userDAO, CompositeDisposable cd,
                                                @ApplicationContext Context context){
        return new HomeRepositoryImpl(userDAO, cd, context);
    }
}

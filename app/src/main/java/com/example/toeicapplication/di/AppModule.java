package com.example.toeicapplication.di;

import android.content.Context;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.db.dao.UserDAO;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.repository.HomeRepositoryImpl;
import com.example.toeicapplication.repository.LoginRepository;
import com.example.toeicapplication.repository.LoginRepositoryImpl;
import com.example.toeicapplication.utilities.MyActivityForResult;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;
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
    public HomeRepository provideHomeRepository(UserService userService,
                                                MyDB database,
                                                CompositeDisposable cd,
                                                @ApplicationContext Context context){
        return new HomeRepositoryImpl(userService, database, cd, context);
    }
}

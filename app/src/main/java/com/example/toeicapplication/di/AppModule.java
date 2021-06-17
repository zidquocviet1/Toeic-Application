package com.example.toeicapplication.di;

import android.content.Context;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.ExamRepository;
import com.example.toeicapplication.repository.impl.ExamRepositoryImpl;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.repository.impl.HomeRepositoryImpl;
import com.example.toeicapplication.repository.LoginRepository;
import com.example.toeicapplication.repository.impl.LoginRepositoryImpl;

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
    public LoginRepository provideLoginRepository(UserService us){
        return new LoginRepositoryImpl(us);
    }

    @Singleton
    @Provides
    public HomeRepository provideHomeRepository(UserService userService,
                                                MyDB database,
                                                CompositeDisposable cd,
                                                @ApplicationContext Context context){
        return new HomeRepositoryImpl(userService, database, cd, context);
    }

    @Singleton
    @Provides
    public ExamRepository provideExamRepository(MyDB database, CompositeDisposable cd){
        return new ExamRepositoryImpl(database, cd);
    }
}

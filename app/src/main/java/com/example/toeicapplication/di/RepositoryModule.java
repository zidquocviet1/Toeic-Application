package com.example.toeicapplication.di;

import android.content.Context;

import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.db.dao.ResultDAO;
import com.example.toeicapplication.network.service.ResultService;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.ExamRepository;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.repository.LoginRepository;
import com.example.toeicapplication.repository.ResultRepository;
import com.example.toeicapplication.repository.impl.ExamRepositoryImpl;
import com.example.toeicapplication.repository.impl.HomeRepositoryImpl;
import com.example.toeicapplication.repository.impl.LoginRepositoryImpl;
import com.example.toeicapplication.repository.impl.ResultRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ViewModelScoped;
import io.reactivex.disposables.CompositeDisposable;

@Module
@InstallIn(ViewModelComponent.class)
public class RepositoryModule {

    @ViewModelScoped
    @Provides
    public ExamRepository provideExamRepository(MyDB database){
        return new ExamRepositoryImpl(database);
    }

    @ViewModelScoped
    @Provides
    public LoginRepository provideLoginRepository(UserService us){
        return new LoginRepositoryImpl(us);
    }

    @ViewModelScoped
    @Provides
    public HomeRepository provideHomeRepository(UserService userService,
                                                MyDB database,
                                                CompositeDisposable cd,
                                                @ApplicationContext Context context){
        return new HomeRepositoryImpl(userService, database, cd, context);
    }

    @ViewModelScoped
    @Provides
    public ResultRepository provideResultRepository(ResultDAO dao, ResultService service){
        return new ResultRepositoryImpl(dao, service);
    }
}

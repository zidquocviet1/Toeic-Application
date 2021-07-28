package com.example.toeicapplication.di;


import com.example.toeicapplication.db.MyDB;
import com.example.toeicapplication.db.dao.ResultDAO;
import com.example.toeicapplication.network.service.CommentService;
import com.example.toeicapplication.network.service.CourseService;
import com.example.toeicapplication.network.service.ResultService;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.repository.CommentRepository;
import com.example.toeicapplication.repository.CourseDetailRepository;
import com.example.toeicapplication.repository.EditProfileRepository;
import com.example.toeicapplication.repository.ExamRepository;
import com.example.toeicapplication.repository.HomeRepository;
import com.example.toeicapplication.repository.LoginRepository;
import com.example.toeicapplication.repository.ResultRepository;
import com.example.toeicapplication.repository.UserInfoRepository;
import com.example.toeicapplication.repository.impl.CommentRepositoryImpl;
import com.example.toeicapplication.repository.impl.CourseDetailRepositoryImpl;
import com.example.toeicapplication.repository.impl.EditProfileRepositoryImpl;
import com.example.toeicapplication.repository.impl.ExamRepositoryImpl;
import com.example.toeicapplication.repository.impl.HomeRepositoryImpl;
import com.example.toeicapplication.repository.impl.LoginRepositoryImpl;
import com.example.toeicapplication.repository.impl.ResultRepositoryImpl;
import com.example.toeicapplication.repository.impl.UserInfoRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
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
                                                CommentService commentService,
                                                MyDB database,
                                                CompositeDisposable cd){
        return new HomeRepositoryImpl(userService, commentService, database, cd);
    }

    @ViewModelScoped
    @Provides
    public ResultRepository provideResultRepository(ResultDAO dao, ResultService service){
        return new ResultRepositoryImpl(dao, service);
    }

    @ViewModelScoped
    @Provides
    public EditProfileRepository provideEditProfileRepository(UserService service, MyDB db){
        return new EditProfileRepositoryImpl(service, db);
    }

    @ViewModelScoped
    @Provides
    public UserInfoRepository provideUserInfoRepository(MyDB db, ResultService resultService, UserService userService){
        return new UserInfoRepositoryImpl(db, resultService, userService);
    }

    @ViewModelScoped
    @Provides
    public CourseDetailRepository provideCourseDetailRepository(CourseService courseService, CommentService commentService){
        return new CourseDetailRepositoryImpl(courseService, commentService);
    }

    @ViewModelScoped
    @Provides
    public CommentRepository provideCommentRepository(CommentService commentService){
        return new CommentRepositoryImpl(commentService);
    }
}

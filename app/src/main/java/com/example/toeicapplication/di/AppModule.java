package com.example.toeicapplication.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.disposables.CompositeDisposable;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Singleton
    @Provides
    public CompositeDisposable provideCD(){
        // user compositeDisposable in Singleton Scope may occur wrong logic.
        return new CompositeDisposable();
    }
}

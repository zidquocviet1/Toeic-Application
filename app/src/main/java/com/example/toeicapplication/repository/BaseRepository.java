package com.example.toeicapplication.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface BaseRepository<T, ID> {
    Flowable<List<T>> getAll();

    Single<T> findById(ID id);

    Single<T> update(T t);

    Completable add(T t);

    Completable delete(ID id);
}

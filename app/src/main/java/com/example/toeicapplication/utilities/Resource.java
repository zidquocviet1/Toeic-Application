package com.example.toeicapplication.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T>{
    @NonNull
    private final Status status;
    @Nullable
    private final T data;
    @Nullable
    private final String message;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> Success(T data){
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> Error(String message){
        return new Resource<>(Status.ERROR, null, message);
    }

    public static <T> Resource<T> Loading(T data){
        return new Resource<>(Status.LOADING, data, null);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}

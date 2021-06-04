package com.example.toeicapplication.utilities;

public class DataState<T>{
    public enum Status{
        SUCCESS,
        ERROR,
        LOADING
    }

    private Status status;
    private T data;
    private String message;

    public DataState(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> DataState<T> Success(T data){
        return new DataState<>(Status.SUCCESS, data, null);
    }

    public static <T> DataState<T> Error(String message){
        return new DataState<>(Status.ERROR, null, message);
    }

    public static <T> DataState<T> Loading(T data){
        return new DataState<>(Status.LOADING, data, null);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

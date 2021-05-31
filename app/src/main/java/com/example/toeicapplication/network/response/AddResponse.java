package com.example.toeicapplication.network.response;

import com.google.gson.annotations.SerializedName;

public class AddResponse<T> extends Response{
    @SerializedName("data")
    private T data;

    public AddResponse(){
        super();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

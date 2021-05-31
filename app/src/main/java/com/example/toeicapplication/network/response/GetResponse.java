package com.example.toeicapplication.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetResponse<T> extends Response {
    @SerializedName("data")
    private List<T> data;

    public GetResponse(List<T> data) {
        super();
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

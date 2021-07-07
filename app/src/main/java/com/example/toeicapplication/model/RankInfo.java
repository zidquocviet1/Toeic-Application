package com.example.toeicapplication.model;

import com.example.toeicapplication.model.entity.RemoteUser;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RankInfo {
    @SerializedName("user")
    private RemoteUser user;
    @SerializedName("result")
    private Result result;

    public RankInfo(RemoteUser user, Result result) {
        this.user = user;
        this.result = result;
    }

    public RemoteUser getUser() {
        return user;
    }

    public void setUser(RemoteUser user) {
        this.user = user;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankInfo rankInfo = (RankInfo) o;
        return Objects.equals(user, rankInfo.user) &&
                Objects.equals(result, rankInfo.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, result);
    }
}

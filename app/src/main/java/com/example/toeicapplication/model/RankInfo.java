package com.example.toeicapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.toeicapplication.model.entity.RemoteUser;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RankInfo implements Parcelable {
    @SerializedName("user")
    private RemoteUser user;
    @SerializedName("result")
    private Result result;

    public RankInfo(RemoteUser user, Result result) {
        this.user = user;
        this.result = result;
    }

    protected RankInfo(Parcel in) {
        user = in.readParcelable(RemoteUser.class.getClassLoader());
        result = in.readParcelable(Result.class.getClassLoader());
    }

    public static final Creator<RankInfo> CREATOR = new Creator<RankInfo>() {
        @Override
        public RankInfo createFromParcel(Parcel in) {
            return new RankInfo(in);
        }

        @Override
        public RankInfo[] newArray(int size) {
            return new RankInfo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeParcelable(result, flags);
    }
}

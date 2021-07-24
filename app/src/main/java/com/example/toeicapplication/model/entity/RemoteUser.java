package com.example.toeicapplication.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

@Entity(tableName = "remote_user", indices = {@Index(name = "remote_user_name_unique", value = {"user_name"}, unique = true)})
public class RemoteUser implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "user_name")
    @SerializedName(value = "userName")
    private String userName;

    @ColumnInfo(name = "display_name")
    @SerializedName(value = "displayName")
    private String displayName;

    @ColumnInfo(name = "biography", defaultValue = "")
    private String biography;

    @ColumnInfo(name = "timestamp")
    private LocalDateTime timestamp;

    public RemoteUser(Long id, String userName, String displayName, String biography, LocalDateTime timestamp) {
        this.id = id;
        this.userName = userName;
        this.displayName = displayName;
        this.biography = biography;
        this.timestamp = timestamp;
    }

    protected RemoteUser(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userName = in.readString();
        displayName = in.readString();
        biography = in.readString();
        timestamp = (LocalDateTime) in.readSerializable();
    }

    public static final Creator<RemoteUser> CREATOR = new Creator<RemoteUser>() {
        @Override
        public RemoteUser createFromParcel(Parcel in) {
            return new RemoteUser(in);
        }

        @Override
        public RemoteUser[] newArray(int size) {
            return new RemoteUser[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(userName);
        dest.writeString(displayName);
        dest.writeString(biography);
        dest.writeSerializable(timestamp);
    }
}

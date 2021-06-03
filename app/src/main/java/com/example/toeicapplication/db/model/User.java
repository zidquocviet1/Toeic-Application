package com.example.toeicapplication.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.toeicapplication.db.converter.LocalDateConverter;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

@Entity(tableName = "user_info")
@TypeConverters(LocalDateConverter.class)
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "user_name")
    @SerializedName(value = "userName")
    private String userName;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "display_name")
    @SerializedName(value = "displayName")
    private String displayName;
    @ColumnInfo(name = "timestamp")
    private LocalDate timestamp;
    @ColumnInfo(name = "login")
    @SerializedName(value = "login")
    private boolean isLogin;

    public User(Long id, String userName, String password, String displayName, LocalDate timestamp, boolean isLogin) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
        this.timestamp = timestamp;
        this.isLogin = isLogin;
    }

    @Ignore
    public User(String userName, String password, String displayName, LocalDate timestamp, boolean isLogin) {
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
        this.timestamp = timestamp;
        this.isLogin = isLogin;
    }

    @Ignore
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Ignore
    public User(User b) {
        this.userName = b.userName;
        this.password = b.password;
        this.displayName = b.displayName;
        this.timestamp = b.timestamp;
        this.isLogin = b.isLogin;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userName = in.readString();
        password = in.readString();
        displayName = in.readString();
        isLogin = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
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
        dest.writeString(password);
        dest.writeString(displayName);
        dest.writeByte((byte) (isLogin ? 1 : 0));
    }
}

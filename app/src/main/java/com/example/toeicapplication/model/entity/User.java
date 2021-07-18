package com.example.toeicapplication.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity(tableName = "user_info", indices = {@Index(name = "user_name_unique", value = {"user_name"}, unique = true)})
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

    @ColumnInfo(name = "biography", defaultValue = "")
    private String biography;

    @ColumnInfo(name = "address", defaultValue = "")
    private String address;

    @ColumnInfo(name = "avatar_path", defaultValue = "")
    @SerializedName("avatar_path")
    private String avatarPath;

    @ColumnInfo(name = "cover_path", defaultValue = "")
    @SerializedName("cover_path")
    private String coverPath;

    @ColumnInfo(name = "birthday", defaultValue = "")
    private LocalDateTime birthday;

    @ColumnInfo(name = "timestamp")
    private LocalDateTime timestamp;

    @ColumnInfo(name = "last_modified")
    @SerializedName(value = "last_modified")
    private LocalDateTime lastModified;

    @ColumnInfo(name = "login")
    @SerializedName(value = "login")
    private Boolean isLogin;

    @ColumnInfo(name = "result_list")
    private ArrayList<Result> results;

    public User(Long id, String userName, String password,
                String displayName, String biography, String address, String avatarPath,
                String coverPath, LocalDateTime birthday, LocalDateTime timestamp,
                LocalDateTime lastModified, Boolean isLogin, ArrayList<Result> results) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
        this.biography = biography;
        this.address = address;
        this.avatarPath = avatarPath;
        this.coverPath = coverPath;
        this.birthday = birthday;
        this.timestamp = timestamp;
        this.lastModified = lastModified;
        this.isLogin = isLogin;
        this.results = results;
    }

    @Ignore
    public User(Long id, String userName, String password,
                String displayName, LocalDateTime timestamp, Boolean isLogin, ArrayList<Result> results) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
        this.timestamp = timestamp;
        this.isLogin = isLogin;
        this.results = results;
    }

    @Ignore
    public User(String userName, String password, String displayName,
                LocalDateTime timestamp, Boolean isLogin, ArrayList<Result> results) {
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
        this.timestamp = timestamp;
        this.isLogin = isLogin;
        this.results = results;
    }

    @Ignore
    public User(String userName, String password, String displayName,
                LocalDateTime timestamp, Boolean isLogin) {
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

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userName = in.readString();
        password = in.readString();
        displayName = in.readString();
        biography = in.readString();
        address = in.readString();
        avatarPath = in.readString();
        coverPath = in.readString();
        byte tmpIsLogin = in.readByte();
        isLogin = tmpIsLogin == 0 ? null : tmpIsLogin == 1;
        results = in.createTypedArrayList(Result.CREATOR);
        timestamp = (LocalDateTime) in.readSerializable();
        birthday = (LocalDateTime) in.readSerializable();
        lastModified = (LocalDateTime) in.readSerializable();
    }

    // parcelable write/read depends on ORDER
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

    public User() {

    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean isLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
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
        dest.writeString(biography);
        dest.writeString(address);
        dest.writeString(avatarPath);
        dest.writeString(coverPath);
        dest.writeByte((byte) (isLogin == null ? 0 : isLogin ? 1 : 2));
        dest.writeTypedList(results);
        dest.writeSerializable(timestamp);
        dest.writeSerializable(birthday);
        dest.writeSerializable(lastModified);
    }
}

package com.example.toeicapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.toeicapplication.db.converter.ArrayListConverter;

import java.util.ArrayList;

@Entity(tableName = "course")
public class Course implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @ColumnInfo(defaultValue = "5")
    private float rating;
    @TypeConverters(ArrayListConverter.class)
    private ArrayList<Comment> comment;

    public Course(long id, @NonNull String name, @NonNull String description, float rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    @Ignore
    public Course(long id, @NonNull String name, @NonNull String description, float rating, ArrayList<Comment> comment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.comment = comment;
    }


    protected Course(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public static final Course[] courses = {
            new Course(0, "Toeic 750", "Mục tiêu 750 điểm", 0),
            new Course(0, "Toeic 550", "Mục tiêu 550 điểm", 0),
            new Course(0, "Toeic 850", "Mục tiêu 850 điểm", 0),
            new Course(0, "Toeic 990", "Mục tiêu 990 điểm", 0),
            new Course(0, "Toeic 650", "Mục tiêu 650 điểm", 0)
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(rating);
    }

    @Override
    public String toString() {
        return name;
    }
}

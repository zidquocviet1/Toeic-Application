package com.example.toeicapplication.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.toeicapplication.db.converter.ArrayListConverter;
import com.example.toeicapplication.model.Comment;

import java.util.ArrayList;

@Entity(tableName = "course")
public class Course implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @ColumnInfo(defaultValue = "null")
    private Float rating;
    @TypeConverters(ArrayListConverter.class)
    private ArrayList<Comment> comment;

    public Course(Long id, @NonNull String name, @NonNull String description, Float rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    @Ignore
    public Course(Long id, @NonNull String name, @NonNull String description, Float rating, ArrayList<Comment> comment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.comment = comment;
    }


    protected Course(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readFloat();
        }
        comment = in.createTypedArrayList(Comment.CREATOR);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public static final Course[] courses = {
            new Course(null, "Toeic 750", "Mục tiêu 750 điểm", null),
            new Course(null, "Toeic 550", "Mục tiêu 550 điểm", null),
            new Course(null, "Toeic 850", "Mục tiêu 850 điểm", null),
            new Course(null, "Toeic 990", "Mục tiêu 990 điểm", null),
            new Course(null, "Toeic 650", "Mục tiêu 650 điểm", null)
    };

    @Override
    public String toString() {
        return name;
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
        dest.writeString(name);
        dest.writeString(description);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(rating);
        }
        dest.writeTypedList(comment);
    }
}

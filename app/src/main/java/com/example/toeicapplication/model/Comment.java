package com.example.toeicapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;

import java.time.LocalDateTime;

public class Comment implements Parcelable {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long courseId;
    private Long userId;

    public Comment(Long id, String content, LocalDateTime timestamp, Long courseId, Long userId) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.courseId = courseId;
        this.userId = userId;
    }

    protected Comment(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        content = in.readString();
        if (in.readByte() == 0) {
            courseId = null;
        } else {
            courseId = in.readLong();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
        }
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        dest.writeString(content);
        if (courseId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(courseId);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userId);
        }
    }
}

package com.example.toeicapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private int id;
    private String content;
    private int rating;
    private long courseId;
    private Long userId;
    private Course course;
    private User user;

    public Comment(int id, String content, int rating, long courseId, Long userId, Course course, User user) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.courseId = courseId;
        this.userId = userId;
        this.course = course;
        this.user = user;
    }

    protected Comment(Parcel in) {
        id = in.readInt();
        content = in.readString();
        rating = in.readInt();
        courseId = in.readLong();
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
        }
        course = in.readParcelable(Course.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeInt(rating);
        dest.writeLong(courseId);
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userId);
        }
        dest.writeParcelable(course, flags);
        dest.writeParcelable(user, flags);
    }
}

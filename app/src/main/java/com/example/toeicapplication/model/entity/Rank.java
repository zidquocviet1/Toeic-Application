package com.example.toeicapplication.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "rank",
        foreignKeys = {
            @ForeignKey(
                    entity = Course.class,
                    parentColumns = "id",
                    childColumns = "courseId",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "id",
                    childColumns = "userId")
})
public class Rank {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long courseId;
    private Long userId;

    public Rank(Long id, Long courseId, Long userId) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

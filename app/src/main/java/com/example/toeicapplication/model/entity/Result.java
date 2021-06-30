package com.example.toeicapplication.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.toeicapplication.db.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;

@Entity(tableName = "result", foreignKeys = {
        @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId"
        ),
        @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId"
        )
})
public class Result implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long userId;
    private Long courseId;
    @ColumnInfo(defaultValue = "0")
    private Integer score;
    @ColumnInfo(name = "reading_score", defaultValue = "0")
    private Integer readingScore;
    @ColumnInfo(name = "listening_score", defaultValue = "0")
    private Integer listeningScore;
    @ColumnInfo(defaultValue = "0")
    private Integer correct;
    @ColumnInfo(defaultValue = "0")
    private Integer wrong;
    @ColumnInfo(defaultValue = "0", name = "total_question")
    private Integer totalQuestion;
    @ColumnInfo(defaultValue = "0")
    private Integer completion;
    @ColumnInfo(defaultValue = "0")
    private Integer duration;
    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime timestamp;

    public Result(Long id, Long userId, Long courseId, Integer score,
                  Integer readingScore, Integer listeningScore, Integer correct,
                  Integer wrong, Integer totalQuestion, Integer completion,
                  Integer duration, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.score = score;
        this.readingScore = readingScore;
        this.listeningScore = listeningScore;
        this.correct = correct;
        this.wrong = wrong;
        this.totalQuestion = totalQuestion;
        this.completion = completion;
        this.duration = duration;
        this.timestamp = timestamp;
    }


    protected Result(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
        }
        if (in.readByte() == 0) {
            courseId = null;
        } else {
            courseId = in.readLong();
        }
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readInt();
        }
        if (in.readByte() == 0) {
            readingScore = null;
        } else {
            readingScore = in.readInt();
        }
        if (in.readByte() == 0) {
            listeningScore = null;
        } else {
            listeningScore = in.readInt();
        }
        if (in.readByte() == 0) {
            correct = null;
        } else {
            correct = in.readInt();
        }
        if (in.readByte() == 0) {
            wrong = null;
        } else {
            wrong = in.readInt();
        }
        if (in.readByte() == 0) {
            totalQuestion = null;
        } else {
            totalQuestion = in.readInt();
        }
        if (in.readByte() == 0) {
            completion = null;
        } else {
            completion = in.readInt();
        }
        if (in.readByte() == 0) {
            duration = null;
        } else {
            duration = in.readInt();
        }
        timestamp = (LocalDateTime) in.readSerializable();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getWrong() {
        return wrong;
    }

    public void setWrong(Integer wrong) {
        this.wrong = wrong;
    }

    public Integer getCompletion() {
        return completion;
    }

    public void setCompletion(Integer completion) {
        this.completion = completion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getReadingScore() {
        return readingScore;
    }

    public void setReadingScore(Integer readingScore) {
        this.readingScore = readingScore;
    }

    public Integer getListeningScore() {
        return listeningScore;
    }

    public void setListeningScore(Integer listeningScore) {
        this.listeningScore = listeningScore;
    }

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userId);
        }
        if (courseId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(courseId);
        }
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(score);
        }
        if (readingScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(readingScore);
        }
        if (listeningScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(listeningScore);
        }
        if (correct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(correct);
        }
        if (wrong == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(wrong);
        }
        if (totalQuestion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalQuestion);
        }
        if (completion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(completion);
        }
        if (duration == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(duration);
        }
        dest.writeSerializable(timestamp);
    }
}

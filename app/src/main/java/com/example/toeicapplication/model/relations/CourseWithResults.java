package com.example.toeicapplication.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;

import java.util.List;

public class CourseWithResults {
    @Embedded
    public Course course;
    @Relation(parentColumn = "id", entityColumn = "courseId")
    public List<Result> results;
}

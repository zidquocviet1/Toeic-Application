package com.example.toeicapplication.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.toeicapplication.model.entity.RemoteUser;
import com.example.toeicapplication.model.entity.Result;

import java.util.List;

public class RemoteUserWithResults {
    @Embedded
    public RemoteUser remoteUser;
    @Relation(parentColumn = "id", entityColumn = "userId")
    public List<Result> results;
}

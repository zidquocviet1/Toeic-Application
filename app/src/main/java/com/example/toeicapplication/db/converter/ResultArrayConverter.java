package com.example.toeicapplication.db.converter;

import androidx.room.TypeConverter;

import com.example.toeicapplication.model.entity.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ResultArrayConverter {
    @TypeConverter
    public static ArrayList<Result> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Result>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Result> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}

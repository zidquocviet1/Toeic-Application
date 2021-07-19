package com.example.toeicapplication.db.converter;

import androidx.room.TypeConverter;

import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.network.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ResultArrayConverter {

    @TypeConverter
    public static ArrayList<Result> fromString(String value) {
        GsonBuilder  builder = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        Gson gson = builder.create();

        Type listType = new TypeToken<ArrayList<Result>>() {}.getType();
        return value == null ? null : gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Result> list) {
        GsonBuilder  builder = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        Gson gson = builder.create();

        return list == null ? null : gson.toJson(list);
    }
}

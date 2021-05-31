package com.example.toeicapplication.db.converter;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateConverter {
    @TypeConverter
    public static LocalDate fromTimeStamp(Long value){
        return LocalDate.of(2000, 9, 17);
    }

    @TypeConverter
    public static Long fromLocalDate(LocalDate date){
        return date == null ? null : date.toEpochDay();
    }
}

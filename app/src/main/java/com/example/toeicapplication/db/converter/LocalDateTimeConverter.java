package com.example.toeicapplication.db.converter;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeConverter {
    @TypeConverter
    public static LocalDateTime fromTimeStamp(Long value){
        return value == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC);
    }

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime date){
        return date == null ? null : date.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}

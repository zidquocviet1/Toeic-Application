package com.example.toeicapplication.utilities;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;

public class DatePickerValidator implements CalendarConstraints.DateValidator {
    private final long point;

    private DatePickerValidator(long point) {
        this.point = point;
    }

    /**
     * Returns a {@link CalendarConstraints.DateValidator} which enables days from {@code point}, in
     * UTC milliseconds, forward.
     */
    @NonNull
    public static DatePickerValidator from(long point) {
        return new DatePickerValidator(point);
    }

    /** Part of {@link android.os.Parcelable} requirements. Do not use. */
    public static final Parcelable.Creator<DatePickerValidator> CREATOR =
            new Parcelable.Creator<DatePickerValidator>() {
                @NonNull
                @Override
                public DatePickerValidator createFromParcel(@NonNull Parcel source) {
                    return new DatePickerValidator(source.readLong());
                }

                @NonNull
                @Override
                public DatePickerValidator[] newArray(int size) {
                    return new DatePickerValidator[size];
                }
            };

    @Override
    public boolean isValid(long date) {
        return point >= date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(point);
    }
}

package com.example.toeicapplication.utilities;

import android.os.SystemClock;
import android.util.ArrayMap;

import java.util.concurrent.TimeUnit;

import kotlin.jvm.Synchronized;

public class RateLimiter<KEY> {
    private final long timeOut;
    private final ArrayMap<KEY, Long> timestamps = new ArrayMap<>();

    public RateLimiter(int timeOut, TimeUnit unit){
        this.timeOut = unit.toMillis(timeOut);
    }

    private Long now(){
        return SystemClock.uptimeMillis();
    }

    @Synchronized
    public boolean shouldFetch(KEY key){
        Long lastFetch = timestamps.get(key);
        Long now = now();

        if (lastFetch == null){
            timestamps.put(key, now);
            return true;
        }

        if (now - lastFetch > timeOut){
            timestamps.put(key, now);
            return true;
        }
        return false;
    }

    @Synchronized
    public void reset(KEY key){
        timestamps.remove(key);
    }
}

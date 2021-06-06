package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.toeicapplication.databinding.ActivityExamBinding;
import com.example.toeicapplication.model.Course;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ExamActivity extends AppCompatActivity {
    private ActivityExamBinding binding;
    private int TESTING_TIME = 2*60*60*1000;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Course course = getIntent().getParcelableExtra("course");

        if (course != null){
            binding.txtTitle.setText(course.getName());

            startCountingTime();
        }
    }

    private void startCountingTime(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (TESTING_TIME > 0){
                        binding.pbTime.setProgress(TESTING_TIME);
                        binding.txtDisplayTime.setText(convertTime(TESTING_TIME));
                        TESTING_TIME -= 1000;
                    }else{
                        binding.pbTime.setProgress(0);
                        binding.txtDisplayTime.setText(convertTime(0));
//                        showResultDialog(false);
                    }
                });
            }
        }, 0, 1000);
    }

    private String convertTime(long milliseconds) {
        return String.format(Locale.getDefault(),"%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
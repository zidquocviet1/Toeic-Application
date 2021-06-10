package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.databinding.ActivityCourseDetailBinding;
import com.example.toeicapplication.model.Course;
import com.example.toeicapplication.view.custom.LoadingDialog;

public class CourseDetailActivity extends AppCompatActivity {
    private ActivityCourseDetailBinding binding;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        course = getIntent().getParcelableExtra("course");

        if (course != null) {
            setupUI(course);
        }
    }

    private void setupUI(Course course) {
        binding.imgBackground.setImageDrawable(ContextCompat.getDrawable(this, CourseAdapter.images[0]));
        binding.txtNumStar.setText(String.valueOf(course.getRating()));
        binding.txtCourseDes.setText(course.getDescription());
        binding.txtTitle.setText(course.getName());
    }

    public void enrollCourse(View view) {
        LoadingDialog.showLoadingDialog(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LoadingDialog.dismissDialog();
            Intent intent = new Intent(CourseDetailActivity.this, ExamActivity.class);
            intent.putExtra("course", course);

            startActivity(intent);
        }, 500);
    }
}
package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.adapters.CourseDetailPagerAdapter;
import com.example.toeicapplication.databinding.ActivityCourseDetailBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CourseDetailActivity extends AppCompatActivity {
    private ActivityCourseDetailBinding binding;
    private Course course;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        course = intent.getParcelableExtra("course");
        user = intent.getParcelableExtra("user");

        if (course != null) {
            setupUI(course);
        }
    }

    private void setupUI(Course course) {
        binding.imgBackground.setImageDrawable(ContextCompat.getDrawable(this, CourseAdapter.images[0]));
        binding.imgBackground.setClipToOutline(true);
        binding.txtNumStar.setText(String.valueOf(course.getRating()));
        binding.txtTitle.setText(course.getName());

        TabLayout tab = binding.tabLayout;

        CourseDetailPagerAdapter courseDetailPagerAdapter = new CourseDetailPagerAdapter(getSupportFragmentManager(),
                getLifecycle(), 2, course);
        binding.viewPager.setAdapter(courseDetailPagerAdapter);
        binding.viewPager.setUserInputEnabled(false);
        new TabLayoutMediator(tab, binding.viewPager, (tab1, position) -> {
            if (position == 0){
                tab1.setText(R.string.overview);
            }else if (position == 1){
                tab1.setText(R.string.review);
            }
        }).attach();
    }

    public void enrollCourse(View view) {
        if (!isFinishing()) {
            LoadingDialog.showLoadingDialog(this);

            Intent intent = new Intent(CourseDetailActivity.this, ExamActivity.class);
            intent.putExtra("course", course);
            intent.putExtra("user", user);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                LoadingDialog.dismissDialog();
                startActivity(intent);
                this.finish();
            }, 500);
        }
    }
}
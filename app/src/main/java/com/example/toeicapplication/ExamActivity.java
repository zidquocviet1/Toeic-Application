package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.toeicapplication.databinding.ActivityExamBinding;

public class ExamActivity extends AppCompatActivity {
    private ActivityExamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
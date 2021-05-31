package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.toeicapplication.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.toeicapplication.databinding.ActivityUserBinding;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.view.fragment.UserInfoFragment;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        receiveUserAndShowInfo();
    }

    private void receiveUserAndShowInfo(){
        Intent intent = getIntent();

        User user = intent.getParcelableExtra("user");

        if (user != null){
            openFragment(UserInfoFragment.class, "User Information");
        }
    }

    private void openFragment(Class fragmentClass, String tag){
        Fragment fragment = null;

        try {
            fragment = (UserInfoFragment) fragmentClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.frameLayout.getId(), fragment, tag)
                    .commit();
        }
    }
}
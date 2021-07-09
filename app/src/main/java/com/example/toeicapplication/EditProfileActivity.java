package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.toeicapplication.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerOnClickEvent();
    }

    private void registerOnClickEvent(){
        binding.imgAvatar.setOnClickListener(this);
        binding.layout1.setOnClickListener(this);
        binding.txtCancel.setOnClickListener(this);
        binding.txtSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.imgAvatar.getId()){
            // check the camera and the internal file permission
            // if the permission is granted then open the dialog to choose image
        }else if (id == binding.layout1.getId()){
            // check the permission like Avatar but this is change the panel background
        }else if (id == binding.txtCancel.getId()){
            this.finish();
        }else if (id == binding.txtSave.getId()){
            // save new user information to the database
        }
    }
}
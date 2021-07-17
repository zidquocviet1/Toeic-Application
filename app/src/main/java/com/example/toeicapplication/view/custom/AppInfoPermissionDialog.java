package com.example.toeicapplication.view.custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.toeicapplication.EditProfileActivity;
import com.example.toeicapplication.databinding.DialogAppInfoPermissionBinding;

import org.jetbrains.annotations.NotNull;

public class AppInfoPermissionDialog extends DialogFragment implements View.OnClickListener {
    private DialogAppInfoPermissionBinding binding;
    private EditProfileActivity context;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DialogAppInfoPermissionBinding.inflate(inflater, container, false);
        context = (EditProfileActivity) getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnSettings.setOnClickListener(this);
        binding.txtNotNow.setOnClickListener(this);
    }

    @Override
    public int getTheme() {
        return android.R.style.Theme_Black_NoTitleBar_Fullscreen;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.btnSettings.getId()){
            dismiss();
            context.goToSettings();
        }else if (id == binding.txtNotNow.getId()){
            dismiss();
        }
    }
}

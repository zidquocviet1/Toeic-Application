package com.example.toeicapplication.view.custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.DialogExplainReasonUsePermissionBinding;

import org.jetbrains.annotations.NotNull;

public class ExplainReasonUsePermissionDialog extends DialogFragment implements View.OnClickListener {
    private DialogExplainReasonUsePermissionBinding binding;
    private OnClickListener callback;

    public interface OnClickListener{
        void onButtonContinueClick();
    }

    public void setOnClickListener(OnClickListener callback){
        if (callback != null) this.callback = callback;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DialogExplainReasonUsePermissionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnContinue.setOnClickListener(this);
        binding.txtNotNow.setOnClickListener(this);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.FadeDialogAnimation;
    }

    @Override
    public int getTheme() {
        return android.R.style.Theme_Material_NoActionBar_TranslucentDecor;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.btnContinue.getId()){
            dismiss();
            if (callback != null) callback.onButtonContinueClick();
        }else if (id == binding.txtNotNow.getId()){
            dismiss();
        }
    }
}
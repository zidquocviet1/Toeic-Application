package com.example.toeicapplication.view.custom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.toeicapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class ChooseModeBottomDialogFragment
        extends BottomSheetDialogFragment
        implements View.OnClickListener {
    private ChooseModeListener callback;

    public interface ChooseModeListener{
        void onConfirm(boolean isCounting);

        void onCancel();
    }

    public static ChooseModeBottomDialogFragment newInstance(){
        return new ChooseModeBottomDialogFragment();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_choose_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnNoCount).setOnClickListener(this);
        view.findViewById(R.id.btnCount).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof ChooseModeListener){
            callback = (ChooseModeListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement ChooseModeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (callback != null)
            callback = null;
    }

    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnCount){
            callback.onConfirm(true);
            dismiss();
        }else if (id == R.id.btnNoCount){
            callback.onConfirm(false);
            dismiss();
        }else if (id == R.id.btnCancel){
            callback.onCancel();
            dismiss();
        }
    }
}

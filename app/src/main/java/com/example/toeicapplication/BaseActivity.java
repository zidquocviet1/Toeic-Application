package com.example.toeicapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.example.toeicapplication.utilities.MyActivityForResult;

import org.jetbrains.annotations.NotNull;

public abstract class BaseActivity<VM extends ViewModel, VB extends ViewBinding> extends AppCompatActivity {
    protected VM mVM;
    protected VB mBinding;

    @NonNull
    public abstract Class<VM> getViewModel();

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract VB bindingInflater();

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = bindingInflater();
        mVM = new ViewModelProvider(this).get(getViewModel());
        setContentView(mBinding.getRoot());

        setupObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    public abstract void setupObserver();

    public <I, O> MyActivityForResult<I, O> getActivityForResult(ActivityResultContract<I, O> input){
        return MyActivityForResult.registerActivityForResult(this, input);
    }
}

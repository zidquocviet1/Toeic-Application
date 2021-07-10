package com.example.toeicapplication;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

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
}

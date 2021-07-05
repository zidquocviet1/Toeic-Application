package com.example.toeicapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.example.toeicapplication.utilities.MyActivityForResult;

public abstract class BaseActivity<VM extends ViewModel, VB extends ViewBinding> extends AppCompatActivity {
    protected VM mVM;
    protected VB mBinding;

    protected final MyActivityForResult<Intent, ActivityResult> activityLauncher =
            MyActivityForResult.registerActivityForResult(this);

//    protected final MyActivityForResult<String, Uri> contentLauncher =
//            MyActivityForResult.registerActivityForResult(this, new ActivityResultContracts.GetContent());

    protected final MyActivityForResult<String, Boolean> permissionLauncher =
            MyActivityForResult.registerActivityForResult(this, new ActivityResultContracts.RequestPermission());

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

    public abstract void setupObserver();
}

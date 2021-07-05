package com.example.toeicapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import org.jetbrains.annotations.NotNull;

public abstract class BaseFragment<VM extends ViewModel, VB extends ViewBinding> extends Fragment {
    protected VM mVM;
    protected VB mBinding;

    public abstract VB bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent);
    public abstract Class<VM> getViewModel();
    public abstract FragmentActivity getFragmentActivity();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mVM = new ViewModelProvider(getFragmentActivity()).get(getViewModel());
        mBinding = bindingInflater(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}

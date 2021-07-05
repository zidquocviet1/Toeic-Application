package com.example.toeicapplication.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends ListAdapter<T, VH> {
    protected BaseAdapter(@NonNull @NotNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }
}

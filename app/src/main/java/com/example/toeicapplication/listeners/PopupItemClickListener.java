package com.example.toeicapplication.listeners;

import com.example.toeicapplication.model.entity.User;

// popup listener
public interface PopupItemClickListener {
    void onItemClick(User user, int id, boolean isLogin);
}

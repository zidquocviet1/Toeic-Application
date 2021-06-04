package com.example.toeicapplication.listeners;

import com.example.toeicapplication.model.User;

public interface PopupItemClickListener {
    void onItemClick(User user, int id, boolean isLogin);
}

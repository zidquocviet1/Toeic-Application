package com.example.toeicapplication.listeners;

import com.example.toeicapplication.db.model.User;

public interface PopupItemClickListener {
    void onItemClick(User user, int id, boolean isLogin);
}

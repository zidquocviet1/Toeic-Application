package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SendMessageActivity extends AppCompatActivity {
    TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        txtMessage = findViewById(R.id.txtMessage);
        String text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        txtMessage.setText(text);
    }
}
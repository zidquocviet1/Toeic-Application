package com.example.toeicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.toeicapplication.databinding.ActivityUserBinding;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        receiveUserAndShowInfo();
        binding.imgAvatar.setOnClickListener(this);
        binding.btnEdit.setOnClickListener(this);
    }

    private void receiveUserAndShowInfo(){
        Intent intent = getIntent();

        User user = intent.getParcelableExtra("user");

        if (user != null){
            List<Result> results = user.getResults();

            binding.txtDisplayName.setText(user.getDisplayName());
            binding.txtJoin.setText(getString(R.string.join, user.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE)));

            if (results != null){
                binding.txtRecord.setText(Html.fromHtml(getString(R.string.record, results.size()), Html.FROM_HTML_MODE_COMPACT));
                Result result = results.stream()
                        .max((o1, o2) -> o1.getScore().compareTo(o2.getScore()))
                        .orElse(null);
                Integer maxCore = result == null ? 0 : result.getScore();
                binding.txtScore.setText(Html.fromHtml(getString(R.string.score, maxCore), Html.FROM_HTML_MODE_COMPACT));
            }else{
                binding.txtRecord.setText(Html.fromHtml(getString(R.string.record, 0), Html.FROM_HTML_MODE_COMPACT));
                binding.txtScore.setText(Html.fromHtml(getString(R.string.score, 0), Html.FROM_HTML_MODE_COMPACT));
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.btnEdit.getId()){
            startActivity(new Intent(UserActivity.this, EditProfileActivity.class));
        }else if (id == binding.imgAvatar.getId()){

        }
    }
}
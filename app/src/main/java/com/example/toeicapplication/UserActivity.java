package com.example.toeicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toeicapplication.databinding.ActivityUserBinding;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.MyActivityForResult;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityUserBinding binding;
    private User user;
    private final MyActivityForResult<Intent, ActivityResult> activityLauncher = MyActivityForResult.registerActivityForResult(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        showInfo(user);
        binding.imgAvatar.setOnClickListener(this);
        binding.btnEdit.setOnClickListener(this);
    }

    private void showInfo(User user){
        if (user != null){
            List<Result> results = user.getResults();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_PATTERN);

            binding.txtDisplayName.setText(user.getDisplayName());
            binding.txtJoin.setText(getString(R.string.join, user.getTimestamp().format(formatter)));

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
            Intent intent = new Intent(UserActivity.this, EditProfileActivity.class);
            intent.putExtra("user", user);

            activityLauncher.mLaunch(intent, result -> {
                Intent response = result.getData();

                if (response != null){
                    User user = response.getParcelableExtra("user");
                    if (user != null) showInfo(user);
                }
            });
        }else if (id == binding.imgAvatar.getId()){

        }
    }
}
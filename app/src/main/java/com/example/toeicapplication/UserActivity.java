package com.example.toeicapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.toeicapplication.databinding.ActivityUserBinding;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.MyActivityForResult;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityUserBinding mBinding;
    private User user;
    private final MyActivityForResult<Intent, ActivityResult> activityLauncher = MyActivityForResult.registerActivityForResult(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        showInfo(user);
        mBinding.imgAvatar.setOnClickListener(this);
        mBinding.btnEdit.setOnClickListener(this);
    }

    private void showInfo(User user){
        if (user != null){
            List<Result> results = user.getResults();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_PATTERN);

            mBinding.txtDisplayName.setText(user.getDisplayName());
            mBinding.txtBio.setText(user.getBiography());
            mBinding.txtJoin.setText(getString(R.string.join, user.getTimestamp().format(formatter)));

            if (results != null){
                mBinding.txtRecord.setText(Html.fromHtml(getString(R.string.record, results.size()), Html.FROM_HTML_MODE_COMPACT));
                Result result = results.stream()
                        .max((o1, o2) -> o1.getScore().compareTo(o2.getScore()))
                        .orElse(null);
                Integer maxCore = result == null ? 0 : result.getScore();
                mBinding.txtScore.setText(Html.fromHtml(getString(R.string.score, maxCore), Html.FROM_HTML_MODE_COMPACT));
            }else{
                mBinding.txtRecord.setText(Html.fromHtml(getString(R.string.record, 0), Html.FROM_HTML_MODE_COMPACT));
                mBinding.txtScore.setText(Html.fromHtml(getString(R.string.score, 0), Html.FROM_HTML_MODE_COMPACT));
            }

            String avatarPath = user.getAvatarPath();
            loadAvatar(avatarPath);
        }
    }

    private void loadAvatar(String avatarPath){
        Drawable defaultIcon = ContextCompat.getDrawable(this, R.drawable.ic_gray_account);

        if (avatarPath != null && !avatarPath.equals("")){
            String avatarName = avatarPath.substring(avatarPath.lastIndexOf('\\') + 1);
            String path = getFilesDir() + "/user-photos/" + user.getId() + "/" + avatarName;

            Glide.with(this)
                    .load(new File(path))
                    .error(defaultIcon)
                    .centerCrop()
                    .into(mBinding.imgAvatar);
        }else{
            mBinding.imgAvatar.setImageDrawable(defaultIcon);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBinding.btnEdit.getId()){
            Intent intent = new Intent(UserActivity.this, EditProfileActivity.class);
            intent.putExtra("user", user);

            activityLauncher.mLaunch(intent, result -> {
                Intent response = result.getData();

                if (response != null){
                    User user = response.getParcelableExtra("user");
                    if (user != null) showInfo(user);
                }
            });
        }else if (id == mBinding.imgAvatar.getId()){

        }
    }
}
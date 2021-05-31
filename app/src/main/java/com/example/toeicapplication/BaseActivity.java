package com.example.toeicapplication;

import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toeicapplication.utilities.MyActivityForResult;

public class BaseActivity extends AppCompatActivity {
    protected final MyActivityForResult<Intent, ActivityResult> activityLauncher =
            MyActivityForResult.registerActivityForResult(this);

    protected final MyActivityForResult<String, Uri> contentLauncher =
            MyActivityForResult.registerActivityForResult(this, new ActivityResultContracts.GetContent());

    protected final MyActivityForResult<String, Boolean> permissionLauncher =
            MyActivityForResult.registerActivityForResult(this, new ActivityResultContracts.RequestPermission());
}

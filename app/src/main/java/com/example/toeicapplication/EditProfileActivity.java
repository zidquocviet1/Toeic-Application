package com.example.toeicapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.toeicapplication.databinding.ActivityEditProfileBinding;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.DatePickerValidator;
import com.example.toeicapplication.utilities.ExifUtils;
import com.example.toeicapplication.utilities.MyActivityForResult;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    private final MyActivityForResult<String, Boolean> permissionLauncher =
            MyActivityForResult.registerActivityForResult(this, new ActivityResultContracts.RequestPermission());

    private final MyActivityForResult<Intent, ActivityResult> resultLauncher =
            MyActivityForResult.registerActivityForResult(this);

    private ActivityEditProfileBinding binding;
    private LocalDateTime birthday = null;

    interface ResultCallback{
        void onActivityResult(ActivityResult result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        if (user != null) showInfo(user);

        registerOnClickEvent();
    }

    private void showInfo(User user) {
        binding.edtName.setText(user.getDisplayName());
    }

    private void registerOnClickEvent() {
        binding.imgAvatar.setOnClickListener(this);
        binding.imgCover.setOnClickListener(this);
        binding.txtCancel.setOnClickListener(this);
        binding.txtSave.setOnClickListener(this);
        binding.edtBirthday.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.imgAvatar.getId()) {
            showPopup(binding.imgAvatar);
        } else if (id == binding.imgCover.getId()) {
            // check the permission like Avatar but this is change the panel background
            showPopup(binding.imgCover);
        } else if (id == binding.txtCancel.getId()) {
            this.finish();
        } else if (id == binding.txtSave.getId()) {
            // save new user information to the database
        } else if (id == binding.edtBirthday.getId()) {
            showDatePicker();
        }
    }

    private void showPopup(ImageView img) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(this);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            alertDialog.setPositiveButton(R.string.take_photo, (dialog, which) -> takePhoto(img));
        }
        alertDialog.setNegativeButton(R.string.choose_existing_photo, (dialog, which) -> chooseExistingPhoto(img));
        alertDialog.create().show();
    }

    private void takePhoto(ImageView img) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        requestPermission(Manifest.permission.CAMERA, takePictureIntent, result -> {
            Intent data = result.getData();
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap imageBitmap = (Bitmap) bundle.get("data");
                    img.setImageBitmap(imageBitmap);
                }
            }
        });
    }

    private void chooseExistingPhoto(ImageView img) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        if (intent.resolveActivity(getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT < 29){
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, intent, result -> getImageBitmapFromResult(result, img));
            }else {
                resultLauncher.mLaunch(intent, result -> getImageBitmapFromResult(result, img));
            }
        }
    }

    private void getImageBitmapFromResult(ActivityResult result, ImageView img){
        Intent data = result.getData();
        if (data != null){
            Uri fullPhotoUri = data.getData();
            Bitmap rotatedBitmap = ExifUtils.getRotatedBitmap(getContentResolver(), fullPhotoUri);
            img.setImageBitmap(rotatedBitmap);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        calendar.setTimeInMillis(MaterialDatePicker.todayInUtcMilliseconds());
        long today = calendar.getTimeInMillis();

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setEnd(today)
                .setValidator(DatePickerValidator.from(today))
                .build();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select the birthday")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setCalendarConstraints(constraints)
                .build();

        picker.setCancelable(false);
        picker.addOnPositiveButtonClickListener(selection -> {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneOffset.UTC);
            String date = dateTime.format(formatter);

            birthday = dateTime;
            binding.edtBirthday.setText(date);
        });
        picker.show(getSupportFragmentManager(), "picker");
    }

    private void requestPermission(String permissionString, Intent intent, ResultCallback callback) {
        if (ContextCompat.checkSelfPermission(this, permissionString) == PackageManager.PERMISSION_GRANTED) {
            startResultLauncher(intent, callback);
        } else {
            // use shouldShowRequestPermissionRationale can customize the dialog
            permissionLauncher.mLaunch(permissionString, isGranted -> {
                if (isGranted) {
                    startResultLauncher(intent, callback);
                }
            });
        }
    }

    private void startResultLauncher(Intent intent, ResultCallback callback) {
        resultLauncher.mLaunch(intent, result -> {
            if (callback != null) callback.onActivityResult(result);
        });
    }
}
package com.example.toeicapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.databinding.ActivityEditProfileBinding;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.DatePickerValidator;
import com.example.toeicapplication.utilities.ExifUtils;
import com.example.toeicapplication.utilities.MyActivityForResult;
import com.example.toeicapplication.utilities.NetworkController;
import com.example.toeicapplication.utilities.RealPathUtil;
import com.example.toeicapplication.utilities.Status;
import com.example.toeicapplication.view.custom.AppInfoPermissionDialog;
import com.example.toeicapplication.view.custom.ExplainReasonUsePermissionDialog;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.viewmodels.EditProfileViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditProfileActivity extends BaseActivity<EditProfileViewModel, ActivityEditProfileBinding>
        implements View.OnClickListener {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    private final MyActivityForResult<String, Boolean> permissionLauncher =
            MyActivityForResult.registerActivityForResult(this, new ActivityResultContracts.RequestPermission());

    private final MyActivityForResult<Intent, ActivityResult> resultLauncher =
            MyActivityForResult.registerActivityForResult(this);

    private final MyActivityForResult<String[], Map<String, Boolean>> multiplePermissionLauncher =
            MyActivityForResult.registerActivityForResult(this, new ActivityResultContracts.RequestMultiplePermissions());

    private String avatarPath = "";
    private String coverPath = "";
    private Bitmap avatarBitmap;
    private Bitmap coverBitmap;
    private User user;
    private LocalDateTime birthday;
    private boolean hasReadExternalPermission = false;
    private boolean hasWriteExternalPermission = false;
    private boolean hasUserChange = false;

    interface ResultCallback {
        void onActivityResult(ActivityResult result);
    }

    @NonNull
    @NotNull
    @Override
    public Class<EditProfileViewModel> getViewModel() {
        return EditProfileViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public ActivityEditProfileBinding bindingInflater() {
        return ActivityEditProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(AppConstants.TAG, "ActivityEditProfileBinding -> onCreate");

        user = getIntent().getParcelableExtra("user");
        if (user != null) showInfo(user);

        registerOnClickEvent();
    }

    @Override
    public void setupObserver() {
        if (mVM != null) {
            mVM.getResponseUser().observe(this, resource -> {
                Status status = resource.getStatus();

                if (status == Status.LOADING) {
                    LoadingDialog.showLoadingDialog(this);
                } else if (status == Status.SUCCESS) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        hasUserChange = true;
                        User remoteUser = resource.getData();

                        if (remoteUser != null) {
                            this.user.setDisplayName(remoteUser.getDisplayName());
                            this.user.setAddress(remoteUser.getAddress());
                            this.user.setBirthday(remoteUser.getBirthday());
                            this.user.setBiography(remoteUser.getBiography());
                            this.user.setAvatarPath(remoteUser.getAvatarPath());
                            this.user.setCoverPath(remoteUser.getCoverPath());

                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                String avatarName = remoteUser.getAvatarPath();
                                if (avatarBitmap != null && avatarName != null && !avatarName.equals("")) {
                                    String fileName = avatarName.substring(avatarName.lastIndexOf('\\') + 1);

                                    Path dir = Paths.get(getFilesDir() + "/user-photos/" + remoteUser.getId());
                                    if (!Files.exists(dir)) {
                                        try {
                                            Files.createDirectories(dir);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    try {
                                        File myFile = new File(dir.toFile(), fileName);
                                        FileOutputStream fos = new FileOutputStream(myFile);
                                        avatarBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            mVM.updateUserProfile(this.user, this);
                        }
                        LoadingDialog.dismissDialog();
                    }, 1000);
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        LoadingDialog.dismissDialog();
                        Toast.makeText(this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                    }, 1000);
                }
            });
        }
    }

    private void showInfo(User user) {
        mBinding.layoutName.getEditText().setText(user.getDisplayName());
        mBinding.layoutBio.getEditText().setText(user.getBiography());
        mBinding.layoutBirthday.getEditText().setText(user.getBirthday() != null ? user.getBirthday().format(formatter) : "");
        mBinding.layoutAddress.getEditText().setText(user.getAddress());
        String avatarPath = user.getAvatarPath();
        loadAvatar(avatarPath);
    }

    private void registerOnClickEvent() {
        mBinding.imgAvatar.setOnClickListener(this);
        mBinding.imgCover.setOnClickListener(this);
        mBinding.txtCancel.setOnClickListener(this);
        mBinding.txtSave.setOnClickListener(this);
        mBinding.edtBirthday.setOnClickListener(this);
    }

    private void loadAvatar(String avatarPath) {
        Drawable defaultIcon = ContextCompat.getDrawable(this, R.drawable.ic_gray_account);

        if (avatarPath != null && !avatarPath.equals("")) {
            String avatarName = avatarPath.substring(avatarPath.lastIndexOf('\\') + 1);
            String path = getFilesDir() + "/user-photos/" + user.getId() + "/" + avatarName;

//            Glide.with(this)
//                    .load(new File(path))
//                    .centerCrop()
//                    .error(defaultIcon)
//                    .into(mBinding.imgAvatar);

            Glide.with(this).load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
                    .centerCrop()
                    .error(defaultIcon)
                    .fallback(defaultIcon)
                    .signature(new ObjectKey(avatarPath))
                    .into(mBinding.imgAvatar);
        } else {
            mBinding.imgAvatar.setImageDrawable(defaultIcon);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBinding.imgAvatar.getId()) {
            showPopup(mBinding.imgAvatar);
        } else if (id == mBinding.imgCover.getId()) {
            showPopup(mBinding.imgCover);
        } else if (id == mBinding.txtCancel.getId()) {
            if (hasUserChange) {
                Intent intent = new Intent();
                intent.putExtra("user", this.user);
                setResult(RESULT_OK, intent);
            }
            this.finish();
        } else if (id == mBinding.txtSave.getId()) {
            String fullName = mBinding.layoutName.getEditText().getText().toString().trim();
            String bio = mBinding.layoutBio.getEditText().getText().toString().trim();
            String address = mBinding.layoutAddress.getEditText().getText().toString().trim();

            if (NetworkController.isOnline(this))
                mVM.editProfile(user.getId(), fullName, bio, birthday, address, avatarPath);
            else Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show();
        } else if (id == mBinding.edtBirthday.getId()) {
            showDatePicker();
            /*
            insert image with the ContentResolver
            Uri imageCollections = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                imageCollections = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            }else{
                imageCollections = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }

            Log.d(AppConstants.TAG, "Content Uri: " + imageCollections.toString());

            ContentValues cv = new ContentValues();
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, avatarName);
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            cv.put(MediaStore.Images.Media.WIDTH, avatarBitmap.getWidth());
            cv.put(MediaStore.Images.Media.HEIGHT, avatarBitmap.getHeight());

            Uri insertUri = getContentResolver().insert(imageCollections, cv);
            try {
                OutputStream os = getContentResolver().openOutputStream(insertUri);
                if (!avatarBitmap.compress(Bitmap.CompressFormat.JPEG, 95, os)){
                    throw new IOException("Couldn't save bitmap");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }
    }

    private void showPopup(ImageView img) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(this);

        String[] items = new String[2];
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            items[0] = getString(R.string.take_photo);
        }
        items[1] = getString(R.string.choose_existing_photo);

        alertDialog.setItems(items, (dialog, which) -> {
            if (items[which].equals(getString(R.string.take_photo))) {
                dispatchTakePictureIntent(img);
            } else if (items[which].equals(getString(R.string.choose_existing_photo))) {
                chooseExistingPhoto(img);
            }
        });
        alertDialog.create().show();
    }

    private File createImageFile(ImageView img) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        String dir = getFilesDir().getAbsolutePath() + "/user-photos/" + user.getId();
        Path path = Paths.get(dir);

        if (!Files.exists(path)){
            Files.createDirectories(path);
        }

        File storageDir = new File(path.toFile().getAbsolutePath());
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        if (img == mBinding.imgAvatar) avatarPath = image.getAbsolutePath();
        else coverPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(ImageView img){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;

            try {
                photoFile = createImageFile(img);
            }catch(IOException e){
                e.printStackTrace();
            }

            if (photoFile != null){
                File finalPhotoFile = photoFile;

                ResultCallback callback = result -> {
                    Intent data = result.getData();
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            Bitmap imageBitmap = (Bitmap) bundle.get("data");
                            try {
                                FileOutputStream fos = new FileOutputStream(finalPhotoFile);
                                if (imageBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos)){
                                    Glide.with(this)
                                            .load(imageBitmap)
                                            .error(ContextCompat.getDrawable(this, R.drawable.ic_gray_account))
                                            .into(img);

                                    if (img == mBinding.imgAvatar){
                                        avatarBitmap = imageBitmap;
                                    }else{
                                        coverBitmap = imageBitmap;
                                    }
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startResultLauncher(takePictureIntent, callback);
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    ExplainReasonUsePermissionDialog reasonUsePermissionDialog = new ExplainReasonUsePermissionDialog();
                    reasonUsePermissionDialog.setOnClickListener(() -> cameraPermissionResult(takePictureIntent, callback));
                    reasonUsePermissionDialog.show(getSupportFragmentManager(), "app_info");
                } else {
                    cameraPermissionResult(takePictureIntent, callback);
                }
            }
        }
    }

    private void cameraPermissionResult(Intent intent, ResultCallback callback) {
        permissionLauncher.mLaunch(Manifest.permission.CAMERA, isGranted -> {
            if (isGranted) {
                startResultLauncher(intent, callback);
            } else {
                AppInfoPermissionDialog appInfoDialog = new AppInfoPermissionDialog();
                appInfoDialog.show(getSupportFragmentManager(), "app_info");
            }
        });
    }

    private void startResultLauncher(Intent intent, ResultCallback callback) {
        resultLauncher.mLaunch(intent, result -> {
            if (callback != null) callback.onActivityResult(result);
        });
    }

    private void chooseExistingPhoto(ImageView img) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestOrUpdatePermission();
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            resultLauncher.mLaunch(intent, result -> getImageBitmapFromResult(result, img));
        }
    }

    private void getImageBitmapFromResult(ActivityResult result, ImageView img) {
        Intent data = result.getData();
        if (data != null) {
            Uri fullPhotoUri = data.getData();
            Bitmap rotatedBitmap = ExifUtils.getRotatedBitmap(getContentResolver(), fullPhotoUri);
            Glide.with(this)
                    .load(rotatedBitmap)
                    .error(ContextCompat.getDrawable(this, R.drawable.ic_gray_account))
                    .into(img);

            if (img == mBinding.imgAvatar) {
                avatarPath = RealPathUtil.getRealPath(this, fullPhotoUri);
                avatarBitmap = rotatedBitmap;
            } else if (img == mBinding.imgCover) {
                coverPath = RealPathUtil.getRealPath(this, fullPhotoUri);
                coverBitmap = rotatedBitmap;
            }
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
            mBinding.edtBirthday.setText(date);
        });
        picker.show(getSupportFragmentManager(), "picker");
    }

    private void requestOrUpdatePermission() {
        boolean readExternalGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        boolean writeExternalGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        boolean minSDK29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        hasReadExternalPermission = readExternalGranted;
        hasWriteExternalPermission = writeExternalGranted || minSDK29;

        List<String> requestPermission = new ArrayList<>();

        if (!hasReadExternalPermission)
            requestPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!hasWriteExternalPermission)
            requestPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!requestPermission.isEmpty()) {
            multiplePermissionLauncher.mLaunch(requestPermission.toArray(new String[requestPermission.size()]), result -> {
                List<String> deniedPermissions = new ArrayList<>();

                for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                    if (!entry.getValue()) deniedPermissions.add(entry.getKey());
                }

                for (String s2 : deniedPermissions) {
                    if (shouldShowRequestPermissionRationale(s2)) {
                        ExplainReasonUsePermissionDialog reasonUsePermissionDialog = new ExplainReasonUsePermissionDialog();
                        reasonUsePermissionDialog.setOnClickListener(this::requestOrUpdatePermission);
                        reasonUsePermissionDialog.show(getSupportFragmentManager(), "app_info");
                    } else {
                        // permission is denied and never ask again is check
                        // ask user to go to settings and manually allow permissions
                        AppInfoPermissionDialog appInfoDialog = new AppInfoPermissionDialog();
                        appInfoDialog.show(getSupportFragmentManager(), "app_info");
                    }
                }
            });
        }
    }

    public void goToSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(settingsIntent);
    }
}
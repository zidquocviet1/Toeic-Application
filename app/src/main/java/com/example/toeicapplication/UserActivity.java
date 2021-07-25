package com.example.toeicapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.adapters.UserInfoPagerAdapter;
import com.example.toeicapplication.databinding.ActivityUserBinding;
import com.example.toeicapplication.model.entity.RemoteUser;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.MyActivityForResult;
import com.example.toeicapplication.view.custom.AppInfoPermissionDialog;
import com.example.toeicapplication.view.custom.ExplainReasonUsePermissionDialog;
import com.example.toeicapplication.view.fragment.RankFragment;
import com.example.toeicapplication.viewmodels.UserInfoViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserActivity extends BaseActivity<UserInfoViewModel, ActivityUserBinding>
        implements View.OnClickListener {
    private User user;
    private final MyActivityForResult<Intent, ActivityResult> activityLauncher
            = getActivityForResult(new ActivityResultContracts.StartActivityForResult());
    private final MyActivityForResult<String, Boolean> requestPermissionLauncher
            = getActivityForResult(new ActivityResultContracts.RequestPermission());

    @NonNull
    @NotNull
    @Override
    public Class<UserInfoViewModel> getViewModel() {
        return UserInfoViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user;
    }

    @Override
    public ActivityUserBinding bindingInflater() {
        return ActivityUserBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserFromIntent();
        if (user == null) this.finish();
        mVM.setUser(user);
        setupViewPager();
        showInfo(user);
        mBinding.imgAvatar.setOnClickListener(this);
        mBinding.btnEdit.setOnClickListener(this);
    }

    @Override
    public void setupObserver() {
        if (mVM != null) {
            mVM.getResultListLiveData().observe(this, resultList -> {
                if (resultList != null && !resultList.isEmpty()) {
                    mBinding.txtRecord.setText(Html.fromHtml(getString(R.string.record, resultList.size()), Html.FROM_HTML_MODE_COMPACT));
                    Result result = resultList.stream()
                            .max((o1, o2) -> o1.getScore().compareTo(o2.getScore()))
                            .orElse(null);
                    Integer maxCore = result == null ? 0 : result.getScore();
                    mBinding.txtScore.setText(Html.fromHtml(getString(R.string.score, maxCore), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    mBinding.txtRecord.setText(Html.fromHtml(getString(R.string.record, 0), Html.FROM_HTML_MODE_COMPACT));
                    mBinding.txtScore.setText(Html.fromHtml(getString(R.string.score, 0), Html.FROM_HTML_MODE_COMPACT));
                }
            });
        }
    }

    private void getUserFromIntent() {
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");

        if (source.equals(HomeActivity.class.getSimpleName())) {
            user = intent.getParcelableExtra("user");
//            mVM.getCourseWithResults();
        } else if (source.equals(RankFragment.class.getSimpleName())) {
            mBinding.btnEdit.setVisibility(View.GONE);
            RemoteUser remoteUser = intent.getParcelableExtra("remote_user");
            user = new User(remoteUser.getId(), remoteUser.getUserName(), "", remoteUser.getDisplayName(),
                    remoteUser.getBiography(), "", "", "", null,
                    remoteUser.getTimestamp(), null, false, null);
            mVM.getResultRemoteWithUserId(user.getId());
        }
    }

    private void setupViewPager() {
        UserInfoPagerAdapter pagerAdapter = new UserInfoPagerAdapter(getSupportFragmentManager(),
                getLifecycle(), 1);

        mBinding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(getString(R.string.record_string));
            }
        }).attach();
    }

    private void showInfo(User user) {
        if (user != null) {
            List<Result> results = user.getResults();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_PATTERN);

            mBinding.txtDisplayName.setText(user.getDisplayName());
            mBinding.txtBio.setText(user.getBiography());
            mBinding.txtJoin.setText(getString(R.string.join, user.getTimestamp().format(formatter)));

            if (results != null) {
                mBinding.txtRecord.setText(Html.fromHtml(getString(R.string.record, results.size()), Html.FROM_HTML_MODE_COMPACT));
                Result result = results.stream()
                        .max((o1, o2) -> o1.getScore().compareTo(o2.getScore()))
                        .orElse(null);
                Integer maxCore = result == null ? 0 : result.getScore();
                mBinding.txtScore.setText(Html.fromHtml(getString(R.string.score, maxCore), Html.FROM_HTML_MODE_COMPACT));
            } else {
                mBinding.txtRecord.setText(Html.fromHtml(getString(R.string.record, 0), Html.FROM_HTML_MODE_COMPACT));
                mBinding.txtScore.setText(Html.fromHtml(getString(R.string.score, 0), Html.FROM_HTML_MODE_COMPACT));
            }

            loadAvatar();
        }
    }

    private void loadAvatar() {
        Drawable defaultIcon = ContextCompat.getDrawable(this, R.drawable.ic_gray_account);

        Glide.with(this).load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
                .centerCrop()
                .error(defaultIcon)
                .transition(DrawableTransitionOptions.withCrossFade())
                .signature(new ObjectKey(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId()))
                .into(mBinding.imgAvatar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBinding.btnEdit.getId()) {
            Intent intent = new Intent(UserActivity.this, EditProfileActivity.class);
            intent.putExtra("user", user);

            activityLauncher.mLaunch(intent, result -> {
                Intent response = result.getData();

                if (response != null) {
                    User user = response.getParcelableExtra("user");
                    if (user != null) showInfo(user);
                }
            });
        } else if (id == mBinding.imgAvatar.getId()) {
            Dialog dialog = new Dialog(this, android.R.style.Theme_Material_NoActionBar_TranslucentDecor);
            dialog.setContentView(R.layout.dialog_image_preview);
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlideDialogAnimation;

            ImageView img = dialog.findViewById(R.id.imgAvatar);
            ImageView imgBack = dialog.findViewById(R.id.imgBack);
            ImageView imgMoreVert = dialog.findViewById(R.id.imgMoreVert);

            imgBack.setOnClickListener(v12 -> dialog.dismiss());
            imgMoreVert.setOnClickListener(v1 -> {
                PopupMenu popup = new PopupMenu(this, imgMoreVert);
                popup.inflate(R.menu.mn_profile_image);
                popup.setOnMenuItemClickListener(item -> {
                    int id1 = item.getItemId();

                    if (id1 == R.id.mnSave) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            saveProfileImage();
                        }else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            ExplainReasonUsePermissionDialog explainDialog = new ExplainReasonUsePermissionDialog();
                            explainDialog.show(getSupportFragmentManager(), "explain");
                        }else{
                            requestPermissionLauncher.mLaunch(Manifest.permission.WRITE_EXTERNAL_STORAGE, isGranted -> {
                                if (isGranted){
                                    saveProfileImage();
                                }else{
                                    AppInfoPermissionDialog appInfoDialog = new AppInfoPermissionDialog();
                                    appInfoDialog.show(getSupportFragmentManager(), "app_info");
                                }
                            });
                        }
                    } else if (id1 == R.id.mnShareImage) {
                        Intent sendLinkIntent = new Intent(Intent.ACTION_SEND);
                        sendLinkIntent.setType("text/plain");
                        sendLinkIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId());
                        startActivity(sendLinkIntent);
                    } else if (id1 == R.id.mnOpenInBrowser) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId()));
                        startActivity(intent);
                    }
                    return false;
                });
                popup.show();
            });

            CircularProgressDrawable cp = new CircularProgressDrawable(this);
            cp.setStrokeWidth(5f);
            cp.setCenterRadius(30f);
            cp.start();

            Glide.with(this).load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
                    .centerCrop()
                    .error(ContextCompat.getDrawable(this, R.drawable.ic_gray_account))
                    .placeholder(cp)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
            dialog.show();
        }
    }

    private void saveProfileImage() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp;

        Glide.with(this)
                .asBitmap()
                .load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(UserActivity.this, getString(R.string.could_not_save_image),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                        Uri imageCollections;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            imageCollections = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                        } else {
                            imageCollections = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }

                        Log.d(AppConstants.TAG, "Content Uri: " + imageCollections.toString());

                        ContentValues cv = new ContentValues();
                        cv.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
                        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        cv.put(MediaStore.Images.Media.WIDTH, resource.getWidth());
                        cv.put(MediaStore.Images.Media.HEIGHT, resource.getHeight());

                        Uri insertUri = getContentResolver().insert(imageCollections, cv);
                        try {
                            OutputStream os = getContentResolver().openOutputStream(insertUri);
                            if (!resource.compress(Bitmap.CompressFormat.JPEG, 95, os)) {
                                throw new IOException(getString(R.string.could_not_save_image));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(UserActivity.this, getString(R.string.save_image_successfully),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                        Toast.makeText(UserActivity.this, getString(R.string.could_not_save_image),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
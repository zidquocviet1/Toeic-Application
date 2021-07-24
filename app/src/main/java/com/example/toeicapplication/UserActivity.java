package com.example.toeicapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.adapters.UserInfoPagerAdapter;
import com.example.toeicapplication.databinding.ActivityUserBinding;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.MyActivityForResult;
import com.example.toeicapplication.viewmodels.UserInfoViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserActivity extends BaseActivity<UserInfoViewModel, ActivityUserBinding>
        implements View.OnClickListener {
    private User user;
    private final MyActivityForResult<Intent, ActivityResult> activityLauncher
            = MyActivityForResult.registerActivityForResult(this);

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

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        if (user == null) this.finish();

        mVM.setUser(user);
        showInfo(user);

        mBinding.imgAvatar.setOnClickListener(this);
        mBinding.btnEdit.setOnClickListener(this);

        UserInfoPagerAdapter pagerAdapter = new UserInfoPagerAdapter(getSupportFragmentManager(),
                getLifecycle(), 1);

        mBinding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager, (tab, position) -> {
            if (position == 0){
                tab.setText(getString(R.string.record_string));
            }
        }).attach();
    }

    @Override
    public void setupObserver() {

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

        Glide.with(this).load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
                .centerCrop()
                .error(defaultIcon)
                .fallback(defaultIcon)
                .signature(new ObjectKey(avatarPath))
                .into(mBinding.imgAvatar);
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

                    if (id1 == R.id.mnSave){
                        saveProfileImage();
                        Log.d(AppConstants.TAG, "Save image into Internal Storage");
                    }else if (id1 == R.id.mnShareImage){
                        Log.d(AppConstants.TAG, "Share image link");
                        Intent sendLinkIntent = new Intent(Intent.ACTION_SEND);
                        sendLinkIntent.setType("text/plain");
                        sendLinkIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId());
                        startActivity(sendLinkIntent);
                    }else if (id1 == R.id.mnOpenInBrowser){
                        Log.d(AppConstants.TAG, "Open image in browser");
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
                    .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                    .into(img);
            dialog.show();
        }
    }

    private void saveProfileImage(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        String dir = getFilesDir().getAbsolutePath() + "/save-photos";
        Path path = Paths.get(dir);

        if (!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.could_not_save_image),
                        Toast.LENGTH_SHORT).show();
            }
        }

        File storageDir = new File(path.toFile().getAbsolutePath());
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.could_not_save_image),
                    Toast.LENGTH_SHORT).show();
        }

        if (image != null){
            try {
                FileOutputStream fos = new FileOutputStream(image);

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
                                if (resource.compress(Bitmap.CompressFormat.JPEG, 95, fos)){
                                    Toast.makeText(UserActivity.this, getString(R.string.save_image_successfully),
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(UserActivity.this, getString(R.string.could_not_save_image),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                                Toast.makeText(UserActivity.this, getString(R.string.could_not_save_image),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                if (image.length() == 0) image.delete();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.could_not_save_image),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
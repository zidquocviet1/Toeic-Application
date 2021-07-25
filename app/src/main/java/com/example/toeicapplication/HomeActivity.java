package com.example.toeicapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.databinding.ActivityHomeBinding;
import com.example.toeicapplication.listeners.PopupItemClickListener;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.MyActivityForResult;
import com.example.toeicapplication.utilities.NetworkController;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.view.fragment.CourseFragment;
import com.example.toeicapplication.view.fragment.HomeFragment;
import com.example.toeicapplication.view.fragment.RankFragment;
import com.example.toeicapplication.view.fragment.VocabularyFragment;
import com.example.toeicapplication.viewmodels.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity
        extends BaseActivity<HomeViewModel, ActivityHomeBinding>
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, PopupItemClickListener {

    private final MyActivityForResult<Intent, ActivityResult> mActivityLauncher =
            MyActivityForResult.registerActivityForResult(this);

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mVM.getNetworkState().postValue(NetworkController.isOnline(context));
        }
    };

    @Override
    public ActivityHomeBinding bindingInflater() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @NonNull
    @NotNull
    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerOnClickEvent();
    }

    private boolean isNetwork() {
        return mVM.getNetworkState().getValue() != null
                && mVM.getNetworkState().getValue();
    }

    private void registerOnClickEvent() {
        mBinding.imageView.setOnClickListener(this);
        mBinding.imgAvatar.setOnClickListener(this);
        mBinding.bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void initDrawerLayout() {
        mBinding.navView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mBinding.drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mBinding.drawerLayout.openDrawer(GravityCompat.START);
        mBinding.navView.setCheckedItem(mBinding.bottomNav.getSelectedItemId());
        mBinding.navView.setNavigationItemSelectedListener(this);
        mBinding.bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void loadAvatar(@NonNull User user, boolean isRemote) {
        String avatarPath = user.getAvatarPath();
        Drawable defaultIcon = ContextCompat.getDrawable(this, R.drawable.ic_gray_account);

        if (avatarPath != null && !avatarPath.equals("")) {
            Glide.with(this).load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
                    .centerCrop()
                    .error(defaultIcon)
                    .fallback(defaultIcon)
                    .signature(new ObjectKey(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId()))
                    .into(mBinding.imgAvatar);

//            if (isRemote) {
//                Glide.with(this).load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
//                        .centerCrop()
//                        .error(defaultIcon)
//                        .fallback(defaultIcon)
//                        .into(mBinding.imgAvatar);
//                Log.d(AppConstants.TAG, "Load avatar from remote");
//            } else {
//                String avatarName = avatarPath.substring(avatarPath.lastIndexOf('\\') + 1);
//                String path = getFilesDir() + "/user-photos/" + user.getId() + "/" + avatarName;
//
//                Glide.with(this).load(new File(path))
//                        .centerCrop()
//                        .error(defaultIcon)
//                        .fallback(defaultIcon)
//                        .into(mBinding.imgAvatar);
//                Log.d(AppConstants.TAG, "Load avatar from local");
//            }
        } else {
            mBinding.imgAvatar.setImageDrawable(defaultIcon);
        }
    }

    public void displayLoading(boolean isDisplay, long time) {
        if (isDisplay) {
            LoadingDialog.showLoadingDialog(this);
        } else {
            new Handler(getMainLooper()).postDelayed(LoadingDialog::dismissDialog, time);
        }
    }

    private void showPopup(User user, boolean isLogin) {
        PopupMenu popup = new PopupMenu(this, mBinding.imgAvatar);
        Menu menu = popup.getMenu();

        if (isLogin) {
            popup.getMenuInflater().inflate(R.menu.account_menu_logout, menu);
            popup.setOnMenuItemClickListener(item -> {
                this.onItemClick(user, item.getItemId(), true);
                return true;
            });
        } else {
            popup.getMenuInflater().inflate(R.menu.account_menu_login, menu);
            popup.setOnMenuItemClickListener(item -> {
                this.onItemClick(user, item.getItemId(), false);
                return true;
            });
        }

        popup.show();
    }

    private void showLogoutDialog(User user) {
        new android.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.account_title))
                .setMessage(getString(R.string.account_message_logout))
                .setIcon(R.drawable.ic_baseline_info_24)
                .setPositiveButton("Yes", (dialog, which) -> logout(user))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void logout(User newUser) {
        newUser.setLogin(false);

        mVM.updateUser(newUser, this);

        if (isNetwork())
            mVM.callLogout(newUser);
    }

    public void openFragment(Class<? extends Fragment> fragmentClass, String tag, int id) {
        Fragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            mBinding.bottomNav.setOnNavigationItemSelectedListener(null);
            mBinding.navView.setNavigationItemSelectedListener(null);

            mBinding.txtTitle.setText(tag);
            mBinding.bottomNav.setSelectedItemId(id);
            mBinding.navView.setCheckedItem(id);
            mBinding.drawerLayout.closeDrawers();

            mBinding.bottomNav.setOnNavigationItemSelectedListener(this);
            mBinding.navView.setNavigationItemSelectedListener(this);

            if (id == R.id.mnVocab
                    && (mVM.getWords().getValue() == null
                    || mVM.getWords().getValue().getData() == null
                    || mVM.getWords().getValue().getData().isEmpty())) {
                mVM.getAllWords();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(mBinding.framelayout.getId(), fragment, tag)
                    .commit();
        }
    }

    private void updateLearnedWord() {
        List<Word> words = mVM.getTop30Words().getValue();
        if (words != null && !words.isEmpty()) {
            mVM.updateLearnedWord(words);
        }
    }

    @Override
    public void setupObserver() {
        if (mVM != null) {
            mVM.getLoginUserLiveData().observe(this, user -> {
                if (user != null) mVM.loadUserFromLocalAndRemote(user, NetworkController.isOnline(this));
            });

            // observe list User from Local Database
            mVM.getLoginUserFromLocalLiveData().observe(this, user -> {
                if (user != null) {
                    /*
                     * This is return a list of User. Then we must filter out the User is login.
                     * Show this user information in the UI. After that, we must make a call to get new state of user
                     * in the remote storage. Then observe it from the code snippet below
                     * */
                    boolean isRemote = user.getLastModified().compareTo(LocalDateTime.now().minusMinutes(1)) >= 0;
                    loadAvatar(user, isRemote);
                }else{
                    mBinding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_gray_account));
                }
            });

            mVM.getCourses().observe(this, courses ->
                    openFragment(HomeFragment.class, getString(R.string.home), R.id.mnHome));
        }
    }

    @Override
    protected void onDestroy() {
        if (receiver != null)
            unregisterReceiver(receiver);

        User cacheUser = mVM.getLoginUserFromLocalLiveData().getValue();
        User recentLogoutUser = mVM.getRecentLogOutUserLiveData().getValue();

        if (isNetwork() && (cacheUser == null || !cacheUser.isLogin()) && recentLogoutUser != null)
            mVM.callLogout(recentLogoutUser);

        updateLearnedWord();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Class<? extends Fragment> fragmentClass = null;
        String tag = "";

        if (id == R.id.mnHome) {
            fragmentClass = HomeFragment.class;
            tag = getString(R.string.home);
        } else if (id == R.id.mnCourse) {
            fragmentClass = CourseFragment.class;
            tag = getString(R.string.courses);
        } else if (id == R.id.mnRank) {
            fragmentClass = RankFragment.class;
            tag = getString(R.string.rank);
        } else if (id == R.id.mnVocab) {
            fragmentClass = VocabularyFragment.class;
            tag = getString(R.string.vocabulary);
        }

        if (mBinding.bottomNav.getSelectedItemId() == id)
            return false;

        openFragment(fragmentClass, tag, id);

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.imageView) {
            initDrawerLayout();
        } else if (id == R.id.imgAvatar) {
            User user = mVM.getLoginUserFromLocalLiveData().getValue();

            if (user != null) {
                showPopup(user, user.isLogin());
            }else{
                showPopup(null, false);
            }
        }
    }

    @Override
    public void onItemClick(User user, int id, boolean isLogin) {
        if (isLogin) {
            if (id == R.id.mnInfo) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("source", HomeActivity.class.getSimpleName());
                startActivity(intent);
            } else if (id == R.id.mnLogout) {
                showLogoutDialog(user);
            }
        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);

            if (id == R.id.mnLogin) {
                intent.putExtra("dest", "Login");
            } else if (id == R.id.mnRegister) {
                intent.putExtra("dest", "Register");
            }

            mActivityLauncher.mLaunch(intent, result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    User responseUser = null;
                    if (data != null) {
                        responseUser = data.getParcelableExtra("user");
                    }

                    if (responseUser != null) {
                        mVM.addUser(responseUser);
                        mVM.updateUserFromLocal(responseUser);
                    }
                }
            });
        }
    }
}
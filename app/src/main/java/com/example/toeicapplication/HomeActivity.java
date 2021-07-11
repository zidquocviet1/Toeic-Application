package com.example.toeicapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.toeicapplication.databinding.ActivityHomeBinding;
import com.example.toeicapplication.listeners.PopupItemClickListener;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.utilities.MyActivityForResult;
import com.example.toeicapplication.utilities.NetworkController;
import com.example.toeicapplication.utilities.Status;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.view.fragment.CourseFragment;
import com.example.toeicapplication.view.fragment.HomeFragment;
import com.example.toeicapplication.view.fragment.RankFragment;
import com.example.toeicapplication.view.fragment.VocabularyFragment;
import com.example.toeicapplication.viewmodels.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

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

    private boolean isNetwork(){
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

    // show the avatar of the user
    private void loadRemoteUser(User user) {

    }

    private void getUserForPopup(List<User> users) {
        users.stream()
                .filter(u -> u != null && u.isLogin())
                .findFirst()
                .map(user -> {
                    showPopup(user, user.isLogin());
                    return user;
                })
                .orElseGet(() -> {
                    showPopup(null, false);
                    return null;
                });
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

        mVM.updateUser(newUser);

        if (isNetwork())
            mVM.callLogout(newUser);
    }

    public void openFragment(Class<? extends Fragment> fragmentClass, String tag, int id){
        Fragment fragment = null;
        try{
            fragment = fragmentClass.newInstance();
        }catch(Exception e){
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
                    || mVM.getWords().getValue().getData().isEmpty())){
                mVM.getAllWords();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(mBinding.framelayout.getId(), fragment, tag)
                    .commit();
        }
    }

    private void updateLearnedWord(){
        List<Word> words = mVM.getTop30Words().getValue();
        if (words != null && !words.isEmpty()) {
            mVM.updateLearnedWord(words);
        }
    }

    @Override
    public void setupObserver() {
        if (mVM != null) {
            // show cache user
            mVM.getUsers().observe(this, users -> {
                if (users != null && !users.isEmpty()) {
                    // get the first user with state is login
                    users.stream()
                            .findFirst()
                            .map(user -> {
                                mVM.getCacheUser().postValue(user);
                                if (isNetwork())
                                    mVM.callRemoteUser(user.getId());
                                return user;
                            })
                            .orElseGet(() -> {
                                mVM.getCacheUser().postValue(null);
                                return null;
                            });
                }
            });

            mVM.getCourses().observe(this, courses ->
                    openFragment(HomeFragment.class, getString(R.string.home), R.id.mnHome));

            mVM.getRemoteUser().observe(this, stateUser -> {
                if (stateUser != null){
                    if (stateUser.getStatus() == Status.SUCCESS) {
                        mVM.getCacheUser().postValue(stateUser.getData());
                        loadRemoteUser(stateUser.getData());
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (receiver != null)
            unregisterReceiver(receiver);

        User cacheUser = mVM.getCacheUser().getValue();
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
        }else if (id == R.id.mnVocab){
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
            List<User> users = mVM.getUsers().getValue();

            if (users != null) {
                if (!users.isEmpty()) {
                    getUserForPopup(users);
                } else {
                    showPopup(null, false);
                }
            }
        }
    }

    @Override
    public void onItemClick(User user, int id, boolean isLogin) {
        if (isLogin) {
            if (id == R.id.mnInfo) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                intent.putExtra("user", user);
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

                    if (responseUser != null)
                        mVM.addUser(responseUser);
                }
            });
        }
    }
}
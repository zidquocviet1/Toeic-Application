package com.example.toeicapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.toeicapplication.databinding.ActivityHomeBinding;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.model.Word;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.view.fragment.CourseFragment;
import com.example.toeicapplication.view.fragment.HomeFragment;
import com.example.toeicapplication.listeners.PopupItemClickListener;
import com.example.toeicapplication.utilities.DataState;
import com.example.toeicapplication.utilities.NetworkController;
import com.example.toeicapplication.view.fragment.RankFragment;
import com.example.toeicapplication.view.fragment.VocabularyFragment;
import com.example.toeicapplication.viewmodels.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity
        extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, PopupItemClickListener {

    private static final String TAG = "MainActivity";

    private ActivityHomeBinding binding;
    private HomeViewModel homeVM;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            homeVM.getNetworkState().postValue(NetworkController.isOnline(context));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        registerOnClickEvent();
        setupViewModel();
        setupObserves();
        loadData();
    }

    private void setupViewModel() {
        homeVM = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void setupObserves() {
        if (homeVM != null) {
            // show cache user
            homeVM.getUsers().observe(this, users -> {
                if (users != null && !users.isEmpty()) {
                    // get the first user with state is login
                    users.stream()
                            .filter(u -> u != null && u.isLogin())
                            .findFirst()
                            .map(user -> {
                                homeVM.getCacheUser().postValue(user);
                                if (isNetwork())
                                    homeVM.callRemoteUser(user.getId());
                                return user;
                            })
                            .orElseGet(() -> {
                                homeVM.getCacheUser().postValue(null);
                                return null;
                            });
                }
            });

            homeVM.getCourses().observe(this, courses -> {
                openFragment(HomeFragment.class, getString(R.string.home), R.id.mnHome);
            });

            homeVM.getRemoteUser().observe(this, stateUser -> {
                if (stateUser != null){
                    if (stateUser.getStatus() == DataState.Status.SUCCESS) {
                        homeVM.getCacheUser().postValue(stateUser.getData());
                        loadRemoteUser(stateUser.getData());
                    }
                }
            });
        }
    }

    private boolean isNetwork(){
        return homeVM.getNetworkState().getValue() != null
                && homeVM.getNetworkState().getValue();
    }

    private void registerOnClickEvent() {
        binding.imageView.setOnClickListener(this);
        binding.imgAvatar.setOnClickListener(this);
        binding.bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void initDrawerLayout() {
        binding.navView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.drawerLayout.openDrawer(GravityCompat.START);
        binding.navView.setCheckedItem(binding.bottomNav.getSelectedItemId());
        binding.navView.setNavigationItemSelectedListener(this);
        binding.bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void loadData(){
        if (homeVM != null) {
            homeVM.getAllUsers();
            homeVM.getAllCourses();
            homeVM.get30Words();
            homeVM.getRecentLogOutUser();
        }
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
        PopupMenu popup = new PopupMenu(this, binding.imgAvatar);
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
                .setPositiveButton("Yes", (dialog, which) -> {
                    logout(user);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void logout(User newUser) {
        newUser.setLogin(false);

        homeVM.updateUser(newUser);

        if (isNetwork())
            homeVM.callLogout(newUser);
    }

    public void openFragment(Class fragmentClass, String tag, int id){
        Fragment fragment = null;
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }

        if (fragment != null) {
            binding.bottomNav.setOnNavigationItemSelectedListener(null);
            binding.navView.setNavigationItemSelectedListener(null);

            binding.txtTitle.setText(tag);
            binding.bottomNav.setSelectedItemId(id);
            binding.navView.setCheckedItem(id);
            binding.drawerLayout.closeDrawers();

            binding.bottomNav.setOnNavigationItemSelectedListener(this);
            binding.navView.setNavigationItemSelectedListener(this);

            if (id == R.id.mnVocab
                    && (homeVM.getWords().getValue() == null
                    || homeVM.getWords().getValue().getData() == null
                    || homeVM.getWords().getValue().getData().isEmpty())){
                homeVM.getAllWords();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(binding.framelayout.getId(), fragment, tag)
                    .commit();
        }
    }

    private void updateLearnedWord(){
        List<Word> words = homeVM.getTop30Words().getValue();
        if (words != null && !words.isEmpty()) {
            homeVM.updateLearnedWord(words);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null)
            unregisterReceiver(receiver);

        User cacheUser = homeVM.getCacheUser().getValue();
        User recentLogoutUser = homeVM.getRecentLogOutUserLiveData().getValue();

        if (isNetwork() && (cacheUser == null || !cacheUser.isLogin()) && recentLogoutUser != null)
            homeVM.callLogout(recentLogoutUser);

        updateLearnedWord();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Class fragmentClass = null;
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

        if (binding.bottomNav.getSelectedItemId() == id)
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
            List<User> users = homeVM.getUsers().getValue();

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
                // open account information activity
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            } else if (id == R.id.mnLogout) {
                showLogoutDialog(user);
            }
        } else {
            if (id == R.id.mnLogin) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                activityLauncher.mLaunch(intent, result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        User responseUser = data.getParcelableExtra("user");

                        if (responseUser != null)
                            homeVM.addUser(responseUser);
                    }
                });
            } else if (id == R.id.mnRegister) {
                // open register activity
            }
        }
    }

    // test new method for request permission
    private void requestLocationPermission() {
        String locationString = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else if (shouldShowRequestPermissionRationale(locationString)) {

        }else{
            permissionLauncher.mLaunch(locationString, result -> {
                if (result){

                }else{
                    requestLocationPermission();
                }
            });
        }
    }
}
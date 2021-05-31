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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.toeicapplication.databinding.ActivityHomeBinding;
import com.example.toeicapplication.db.model.User;
import com.example.toeicapplication.fragment.HomeFragment;
import com.example.toeicapplication.listeners.PopupItemClickListener;
import com.example.toeicapplication.utilities.NetworkController;
import com.example.toeicapplication.viewmodels.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
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
            if (NetworkController.isOnline(context)) {

            } else {
                Toast.makeText(HomeActivity.this,
                        getText(R.string.connection), Toast.LENGTH_SHORT).show();
            }
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
        getAllUsers();
        getAllCourses();
        getTop30Words();
        openFragment(HomeFragment.class, "Home");
    }

    private void setupViewModel() {
        homeVM = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void setupObserves() {
        if (homeVM != null) {
            homeVM.getUsers().observe(this, users -> {
                if (users != null && !users.isEmpty()) {
                    // get the first user with state is login
                    users.stream()
                            .filter(u -> u != null && u.isLogin())
                            .findFirst()
                            .map(user -> {
                                homeVM.getOnlineUser().postValue(user);
                                return user;
                            })
                            .orElseGet(() -> {
                                homeVM.getOnlineUser().postValue(null);
                                return null;
                            });
                }
            });
        }
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

    private void getAllUsers() {
        homeVM.getAllUsers();
    }

    private void getAllCourses() {
        homeVM.getAllCourses();
    }

    private void getTop30Words() {
        homeVM.get30Words();
    }

    private void showUserInfo(User user) {

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
    }

    private void openFragment(Class fragmentClass, String tag){
        Fragment fragment = null;
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.framelayout.getId(), fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
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
            tag = "Home";
        } else if (id == R.id.mnCourse) {
            Log.d(TAG, "Course item is clicked!");
        } else if (id == R.id.mnRank) {
            Log.d(TAG, "Rank item is clicked!");
        }

        if (binding.bottomNav.getSelectedItemId() == id)
            return false;

        binding.bottomNav.setOnNavigationItemSelectedListener(null);
        binding.navView.setNavigationItemSelectedListener(null);

        binding.bottomNav.setSelectedItemId(id);
        binding.navView.setCheckedItem(id);
        binding.drawerLayout.closeDrawers();
        openFragment(fragmentClass, tag);

        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        binding.navView.setNavigationItemSelectedListener(this);

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

                        User test = new User(null,
                                "12345678",
                                "12345678",
                                "Mai Quoc Viet",
                                LocalDate.now(),
                                true);
                        if (responseUser == null) {
                            Log.e(TAG, "null user");
                            homeVM.addUser(test);
                        } else {
                            Log.e(TAG, responseUser.toString());
                            homeVM.addUser(test);
                        }
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
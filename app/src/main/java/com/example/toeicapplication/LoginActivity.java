package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.example.toeicapplication.adapters.LoginAdapter;
import com.example.toeicapplication.databinding.ActivityLoginBinding;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.utilities.NetworkController;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.viewmodels.LoginViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity{
    private ActivityLoginBinding binding;
    private LoginViewModel loginVM;

    private TabLayout tb;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loginVM.getNetworkState().postValue(NetworkController.isOnline(context));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        loginVM = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void setupUI(){
        tb = binding.tabLayout;

        LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), getLifecycle(), 2);
        binding.viewPager.setAdapter(loginAdapter);

        new TabLayoutMediator(tb, binding.viewPager, (tab, position) -> {
            if (position == 0){
                tab.setText(R.string.login);
            }else if (position == 1){
                tab.setText(R.string.signup);
            }
        }).attach();

        String dest = getIntent().getStringExtra("dest");
        TabLayout.Tab tab = null;

        if (dest.equals("Login")){
            tab = tb.getTabAt(0);
        }else if (dest.equals("Register")){
            tab = tb.getTabAt(1);
        }

        if (tab != null)
            tab.select();
    }

    public void displayLoading(boolean isDisplay) {
        if (isDisplay) {
            LoadingDialog.showLoadingDialog(this);
        } else {
            new Handler(getMainLooper()).postDelayed(LoadingDialog::dismissDialog, 1000);
        }
    }

    public void navigateHomePage(User user) {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    public void navigateTabLogin(){
        if (tb != null){
            TabLayout.Tab tab = tb.getTabAt(0);
            if (tab != null)
                tab.select();
        }
    }

    @Override
    public void onBackPressed() {
        navigateHomePage(null);
    }

    @Override
    protected void onDestroy() {
        LoadingDialog.dismissDialog();
        if (receiver != null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
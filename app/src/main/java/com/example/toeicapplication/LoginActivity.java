package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.toeicapplication.databinding.ActivityLoginBinding;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.utilities.DataState;
import com.example.toeicapplication.utilities.EncryptPassword;
import com.example.toeicapplication.view.LoadingDialog;
import com.example.toeicapplication.viewmodels.LoginViewModel;

import java.time.LocalDate;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private LoginViewModel loginVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupVMAndObserve();
        binding.btnLogin.setOnClickListener(this);
    }

    private void setupVMAndObserve() {
        loginVM = new ViewModelProvider(this).get(LoginViewModel.class);

        loginVM.getUserName().observe(this, userName -> {
            binding.layoutUsername.getEditText().setText(userName);
        });

        loginVM.getPassword().observe(this, password -> {
            binding.layoutPassword.getEditText().setText(password);
        });

        loginVM.getUserError().observe(this, error -> {
            if (!error.equals("")) {
                binding.layoutUsername.setError(error);
                binding.layoutUsername.getEditText().requestFocus();
                binding.layoutPassword.setErrorEnabled(false);
            }
        });

        loginVM.getPasswordError().observe(this, error -> {
            if (!error.equals("")) {
                binding.layoutPassword.setError(error);
                binding.layoutPassword.getEditText().requestFocus();
                binding.layoutUsername.setErrorEnabled(false);
            }
        });

        loginVM.getStateResponse().observe(this, userDataState -> {
            if (userDataState.getStatus() == DataState.Status.LOADING) {
                displayLoading(true);
            }

            if (userDataState.getStatus() == DataState.Status.SUCCESS) {
                displayLoading(false);
                navigateHomePage(userDataState.getData());
            }

            if (userDataState.getStatus() == DataState.Status.ERROR) {
                displayLoading(false);
                new Handler(getMainLooper()).postDelayed(()
                        -> Toast.makeText(this, userDataState.getMessage(),
                        Toast.LENGTH_SHORT).show(), 1000);
            }
        });
    }

    private void displayLoading(boolean isDisplay) {
        if (isDisplay) {
            LoadingDialog.showLoadingDialog(this);
        } else {
            new Handler(getMainLooper()).postDelayed(LoadingDialog::dismissDialog, 1000);
        }
    }

    private boolean validateUserNameAndPassword(String userName, String password) {
        // check the network state
        if (false) {
            Toast.makeText(this, getText(R.string.connection),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (userName.length() < 8) {
                binding.layoutUsername.setError(getText(R.string.username_error));
                Objects.requireNonNull(binding.layoutUsername.getEditText()).requestFocus();
                binding.layoutPassword.setErrorEnabled(false);
                loginVM.setUserNameError(getText(R.string.username_error).toString());
                return false;
            }
            if (password.length() < 8) {
                binding.layoutPassword.setError(getText(R.string.password_error));
                Objects.requireNonNull(binding.layoutPassword.getEditText()).requestFocus();
                binding.layoutUsername.setErrorEnabled(false);
                loginVM.setPasswordError(getText(R.string.password_error).toString());
                return false;
            }
            binding.layoutUsername.setErrorEnabled(false);
            binding.layoutPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void login(String userName, String password) {
        if (validateUserNameAndPassword(userName, password)) {
            String newPassword = EncryptPassword.encrypt(password);
            User user = new User(userName, newPassword);

            loginVM.login(user, this);
        }
    }

    private void navigateHomePage(User user) {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        navigateHomePage(null);
    }

    @Override
    protected void onDestroy() {
        LoadingDialog.dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnLogin) {
            String userName = binding.layoutUsername.getEditText().getText().toString();
            String password = binding.layoutPassword.getEditText().getText().toString();

            loginVM.setUserName(userName);
            loginVM.setPassword(password);

            login(userName, password);
        }
    }
}
package com.example.toeicapplication.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.toeicapplication.LoginActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.TabSignupFragmentBinding;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.DataState;
import com.example.toeicapplication.utilities.EncryptPassword;
import com.example.toeicapplication.viewmodels.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static android.os.Looper.getMainLooper;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private TabSignupFragmentBinding binding;
    private LoginActivity context;
    private LoginViewModel loginVM;

    private boolean isNetwork = false;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = TabSignupFragmentBinding.inflate(inflater, container, false);
        Log.e("Sign Up Frag: ", "onCreateView");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = (LoginActivity) getActivity();
        Log.e("Sign Up Frag: ", "onViewCreated");
        binding.btnSignup.setOnClickListener(this);
        loginVM = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        setupVMAndObserve();
    }

    private void setupVMAndObserve() {
        loginVM.getUserName().observe(getViewLifecycleOwner(),
                userName -> binding.layoutUsername.getEditText().setText(userName));
        loginVM.getPassword().observe(getViewLifecycleOwner(),
                password -> binding.layoutPassword.getEditText().setText(password));
        loginVM.getDisplayName().observe(getViewLifecycleOwner(),
                displayName -> binding.layoutFullName.getEditText().setText(displayName));
        loginVM.getRePassword().observe(getViewLifecycleOwner(),
                password -> binding.layoutRePassword.getEditText().setText(password));


        loginVM.getUserError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                binding.layoutUsername.setError(error);
                binding.layoutUsername.getEditText().requestFocus();
                binding.layoutFullName.setErrorEnabled(false);
            }
        });
        loginVM.getDisplayNameError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                binding.layoutFullName.setError(error);
                binding.layoutFullName.getEditText().requestFocus();
                binding.layoutPassword.setErrorEnabled(false);
            }
        });
        loginVM.getPasswordError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                binding.layoutPassword.setError(error);
                binding.layoutPassword.getEditText().requestFocus();
                binding.layoutRePassword.setErrorEnabled(false);
            }
        });
        loginVM.getRePasswordError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                binding.layoutRePassword.setError(error);
                binding.layoutRePassword.getEditText().requestFocus();
                binding.layoutPassword.setErrorEnabled(false);
            }
        });
        loginVM.getStateResponse().observe(getViewLifecycleOwner(), userDataState -> {
            if (isResumed()) {
                if (userDataState.getStatus() == DataState.Status.LOADING) {
                    context.displayLoading(true);
                }

                if (userDataState.getStatus() == DataState.Status.SUCCESS) {
                    context.displayLoading(false);

                    new Handler(getMainLooper()).postDelayed(() -> {
                        Toast.makeText(context, R.string.signup_success,
                                Toast.LENGTH_SHORT).show();

                        String userName = binding.layoutUsername.getEditText().getText().toString();
                        String password = binding.layoutPassword.getEditText().getText().toString();

                        loginVM.setLoginUserName(userName);
                        loginVM.setLoginPassword(password);

                        context.navigateTabLogin();
                    }, 1000);
                }

                if (userDataState.getStatus() == DataState.Status.ERROR) {
                    context.displayLoading(false);
                    new Handler(getMainLooper()).postDelayed(() -> {
                        resetState();
                        Toast.makeText(context, userDataState.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }, 1000);
                }
            }
        });

        loginVM.getNetworkState().observe(getViewLifecycleOwner(), status -> this.isNetwork = status);
    }

    private boolean validateUserNameAndPassword(String userName, String password,
                                                String displayName, String rePassword) {
        if (userName.length() < 8) {
            binding.layoutUsername.setError(getText(R.string.username_error));
            binding.layoutUsername.getEditText().requestFocus();
            binding.layoutFullName.setErrorEnabled(false);
            loginVM.setUserNameError(getText(R.string.username_error).toString());
            return false;
        }
        if (displayName.length() == 0) {
            binding.layoutFullName.setError(getText(R.string.display_name_error));
            binding.layoutFullName.getEditText().requestFocus();
            binding.layoutUsername.setErrorEnabled(false);
            loginVM.setDisplayNameError(getText(R.string.display_name_error).toString());
            return false;
        }
        if (password.length() < 8) {
            binding.layoutPassword.setError(getText(R.string.password_error));
            binding.layoutPassword.getEditText().requestFocus();
            binding.layoutFullName.setErrorEnabled(false);
            binding.layoutUsername.setErrorEnabled(false);
            loginVM.setPasswordError(getText(R.string.password_error).toString());
            return false;
        }
        if (rePassword.length() < 8 || !rePassword.equals(password)) {
            binding.layoutRePassword.setError(getText(R.string.re_password));
            binding.layoutRePassword.getEditText().requestFocus();
            binding.layoutPassword.setErrorEnabled(false);
            binding.layoutFullName.setErrorEnabled(false);
            binding.layoutUsername.setErrorEnabled(false);
            loginVM.setRePasswordError(getText(R.string.re_password).toString());
            return false;
        }
        binding.layoutUsername.setErrorEnabled(false);
        binding.layoutPassword.setErrorEnabled(false);
        binding.layoutFullName.setErrorEnabled(false);
        binding.layoutRePassword.setErrorEnabled(false);
        return true;
    }

    private void resetState() {
        loginVM.setUserName("");
        loginVM.setDisplayName("");
        loginVM.setPassword("");
        loginVM.setRePassword("");
        loginVM.setUserNameError("");
        loginVM.setDisplayNameError("");
        loginVM.setPasswordError("");
        loginVM.setRePasswordError("");
        binding.layoutUsername.getEditText().requestFocus();
    }

    private void signUp(String userName, String password,
                        String displayName, String rePassword,
                        LoginActivity context) {
        if (validateUserNameAndPassword(userName, password, displayName, rePassword)) {
            String newPassword = EncryptPassword.encrypt(password);
            User user = new User(userName, newPassword, displayName, LocalDateTime.now(), false);

            if (!isNetwork) {
                Toast.makeText(context, getText(R.string.connection),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            loginVM.signUp(user, context);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.btnSignup.getId()) {
            String userName = binding.layoutUsername.getEditText().getText().toString();
            String password = binding.layoutPassword.getEditText().getText().toString();
            String displayName = binding.layoutFullName.getEditText().getText().toString();
            String rePassword = binding.layoutRePassword.getEditText().getText().toString();

            loginVM.setUserName(userName);
            loginVM.setPassword(password);
            loginVM.setRePassword(rePassword);
            loginVM.setDisplayName(displayName);

            signUp(userName, password, displayName, rePassword, context);
        }
    }
}

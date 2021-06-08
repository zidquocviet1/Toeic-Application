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
import com.example.toeicapplication.databinding.TabLoginFragmentBinding;
import com.example.toeicapplication.model.User;
import com.example.toeicapplication.utilities.DataState;
import com.example.toeicapplication.utilities.EncryptPassword;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.viewmodels.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

import static android.os.Looper.getMainLooper;

@AndroidEntryPoint
public class TabLoginFragment extends Fragment implements View.OnClickListener {
    private TabLoginFragmentBinding binding;
    private LoginActivity context;
    private LoginViewModel loginVM;

    private boolean isNetwork = false;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = TabLoginFragmentBinding.inflate(inflater, container, false);
        Log.e("Login Frag: ", "onCreateView");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = (LoginActivity) getActivity();

        Log.e("Login Frag: ", "onViewCreated");

        binding.btnLogin.setOnClickListener(this);
        loginVM = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        setupVMAndObserve();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Login Frag: ", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Login Frag: ", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Login Frag: ", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Login Frag: ", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Login Frag: ", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Login Frag: ", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Login Frag: ", "onDetach");
    }

    private void resetState() {
        loginVM.setLoginPasswordError("");
        loginVM.setLoginUserNameError("");
        loginVM.setLoginPassword("");
        loginVM.setLoginUserName("");
        binding.layoutUsername.getEditText().requestFocus();
    }

    private void setupVMAndObserve() {
        loginVM.getLoginUserName().observe(getViewLifecycleOwner(), userName -> {
            binding.layoutUsername.getEditText().setText(userName);
        });

        loginVM.getLoginPassword().observe(getViewLifecycleOwner(), password -> {
            binding.layoutPassword.getEditText().setText(password);
        });

        loginVM.getLoginUserNameError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                binding.layoutUsername.setError(error);
                binding.layoutUsername.getEditText().requestFocus();
                binding.layoutPassword.setErrorEnabled(false);
            }
        });

        loginVM.getLoginPasswordError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                binding.layoutPassword.setError(error);
                binding.layoutPassword.getEditText().requestFocus();
                binding.layoutUsername.setErrorEnabled(false);
            }
        });

        loginVM.getStateResponse().observe(getViewLifecycleOwner(), userDataState -> {
            if (isResumed()) {
                if (userDataState.getStatus() == DataState.Status.LOADING) {
                    context.displayLoading(true);
                }

                if (userDataState.getStatus() == DataState.Status.SUCCESS) {
                    context.displayLoading(false);
                    new Handler(getMainLooper()).postDelayed(()
                            -> context.navigateHomePage(userDataState.getData()), 1000);
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

    private boolean validateUserNameAndPassword(String userName, String password) {
        if (userName.length() < 8) {
            binding.layoutUsername.setError(getText(R.string.username_error));
            Objects.requireNonNull(binding.layoutUsername.getEditText()).requestFocus();
            binding.layoutPassword.setErrorEnabled(false);
            loginVM.setLoginUserNameError(getText(R.string.username_error).toString());
            return false;
        }
        if (password.length() < 8) {
            binding.layoutPassword.setError(getText(R.string.password_error));
            Objects.requireNonNull(binding.layoutPassword.getEditText()).requestFocus();
            binding.layoutUsername.setErrorEnabled(false);
            loginVM.setLoginPasswordError(getText(R.string.password_error).toString());
            return false;
        }
        binding.layoutUsername.setErrorEnabled(false);
        binding.layoutPassword.setErrorEnabled(false);
        return true;
    }

    private void login(String userName, String password, LoginActivity context) {
        if (validateUserNameAndPassword(userName, password)) {
            String newPassword = EncryptPassword.encrypt(password);
            User user = new User(userName, newPassword);

            if (!isNetwork) {
                Toast.makeText(context, getText(R.string.connection),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            loginVM.login(user, context);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnLogin) {
            String userName = binding.layoutUsername.getEditText().getText().toString();
            String password = binding.layoutPassword.getEditText().getText().toString();

            loginVM.setLoginUserName(userName);
            loginVM.setLoginPassword(password);

            login(userName, password, context);
        }
    }
}

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
import androidx.fragment.app.FragmentActivity;

import com.example.toeicapplication.LoginActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.TabLoginFragmentBinding;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.EncryptPassword;
import com.example.toeicapplication.utilities.Status;
import com.example.toeicapplication.viewmodels.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

import static android.os.Looper.getMainLooper;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment<LoginViewModel, TabLoginFragmentBinding> implements View.OnClickListener {
    private LoginActivity context;
    private boolean isNetwork = false;

    @Override
    public TabLoginFragmentBinding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return TabLoginFragmentBinding.inflate(inflater, container, attachToParent);
    }

    @Override
    public Class<LoginViewModel> getViewModel() {
        return LoginViewModel.class;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return requireActivity();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = (LoginActivity) getActivity();
        mBinding.btnLogin.setOnClickListener(this);
        setupVMAndObserve();
    }

    private void resetState() {
        mVM.setLoginPasswordError("");
        mVM.setLoginUserNameError("");
        mVM.setLoginPassword("");
        mVM.setLoginUserName("");
        mBinding.layoutUsername.getEditText().requestFocus();
    }

    private void setupVMAndObserve() {
        mVM.getLoginUserName().observe(getViewLifecycleOwner(), userName -> {
            mBinding.layoutUsername.getEditText().setText(userName);
        });

        mVM.getLoginPassword().observe(getViewLifecycleOwner(), password -> {
            mBinding.layoutPassword.getEditText().setText(password);
        });

        mVM.getLoginUserNameError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                mBinding.layoutUsername.setError(error);
                mBinding.layoutUsername.getEditText().requestFocus();
                mBinding.layoutPassword.setErrorEnabled(false);
            }
        });

        mVM.getLoginPasswordError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                mBinding.layoutPassword.setError(error);
                mBinding.layoutPassword.getEditText().requestFocus();
                mBinding.layoutUsername.setErrorEnabled(false);
            }
        });

        mVM.getStateResponse().observe(getViewLifecycleOwner(), userDataState -> {
            if (isResumed()) {
                if (userDataState.getStatus() == Status.LOADING) {
                    context.displayLoading(true);
                }

                if (userDataState.getStatus() == Status.SUCCESS) {
                    context.displayLoading(false);
                    new Handler(getMainLooper()).postDelayed(()
                            -> context.navigateHomePage(userDataState.getData()), 1000);
                }

                if (userDataState.getStatus() == Status.ERROR) {
                    context.displayLoading(false);
                    new Handler(getMainLooper()).postDelayed(() -> {
                        resetState();
                        Toast.makeText(context, userDataState.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }, 1000);
                }
            }
        });

        mVM.getNetworkState().observe(getViewLifecycleOwner(), status -> this.isNetwork = status);
    }

    private boolean validateUserNameAndPassword(String userName, String password) {
        if (userName.length() < 8) {
            mBinding.layoutUsername.setError(getText(R.string.username_error));
            Objects.requireNonNull(mBinding.layoutUsername.getEditText()).requestFocus();
            mBinding.layoutPassword.setErrorEnabled(false);
            mVM.setLoginUserNameError(getText(R.string.username_error).toString());
            return false;
        }
        if (password.length() < 8) {
            mBinding.layoutPassword.setError(getText(R.string.password_error));
            Objects.requireNonNull(mBinding.layoutPassword.getEditText()).requestFocus();
            mBinding.layoutUsername.setErrorEnabled(false);
            mVM.setLoginPasswordError(getText(R.string.password_error).toString());
            return false;
        }
        mBinding.layoutUsername.setErrorEnabled(false);
        mBinding.layoutPassword.setErrorEnabled(false);
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
            mVM.login(user, context);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnLogin) {
            String userName = mBinding.layoutUsername.getEditText().getText().toString();
            String password = mBinding.layoutPassword.getEditText().getText().toString();

            mVM.setLoginUserName(userName);
            mVM.setLoginPassword(password);

            login(userName, password, context);
        }
    }
}

package com.example.toeicapplication.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.toeicapplication.LoginActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.TabSignupFragmentBinding;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.EncryptPassword;
import com.example.toeicapplication.utilities.Status;
import com.example.toeicapplication.viewmodels.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static android.os.Looper.getMainLooper;

public class SignUpFragment extends BaseFragment<LoginViewModel, TabSignupFragmentBinding> implements View.OnClickListener {
    private LoginActivity context;

    private boolean isNetwork = false;

    @Override
    public TabSignupFragmentBinding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return TabSignupFragmentBinding.inflate(inflater, container, attachToParent);
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
        mBinding.btnSignup.setOnClickListener(this);
        setupVMAndObserve();
    }

    private void setupVMAndObserve() {
        mVM.getUserName().observe(getViewLifecycleOwner(),
                userName -> mBinding.layoutUsername.getEditText().setText(userName));
        mVM.getPassword().observe(getViewLifecycleOwner(),
                password -> mBinding.layoutPassword.getEditText().setText(password));
        mVM.getDisplayName().observe(getViewLifecycleOwner(),
                displayName -> mBinding.layoutFullName.getEditText().setText(displayName));
        mVM.getRePassword().observe(getViewLifecycleOwner(),
                password -> mBinding.layoutRePassword.getEditText().setText(password));


        mVM.getUserError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                mBinding.layoutUsername.setError(error);
                mBinding.layoutUsername.getEditText().requestFocus();
                mBinding.layoutFullName.setErrorEnabled(false);
            }
        });
        mVM.getDisplayNameError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                mBinding.layoutFullName.setError(error);
                mBinding.layoutFullName.getEditText().requestFocus();
                mBinding.layoutPassword.setErrorEnabled(false);
            }
        });
        mVM.getPasswordError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                mBinding.layoutPassword.setError(error);
                mBinding.layoutPassword.getEditText().requestFocus();
                mBinding.layoutRePassword.setErrorEnabled(false);
            }
        });
        mVM.getRePasswordError().observe(getViewLifecycleOwner(), error -> {
            if (!error.equals("")) {
                mBinding.layoutRePassword.setError(error);
                mBinding.layoutRePassword.getEditText().requestFocus();
                mBinding.layoutPassword.setErrorEnabled(false);
            }
        });
        mVM.getStateResponse().observe(getViewLifecycleOwner(), userDataState -> {
            if (isResumed()) {
                if (userDataState.getStatus() == Status.LOADING) {
                    context.displayLoading(true);
                }

                if (userDataState.getStatus() == Status.SUCCESS) {
                    context.displayLoading(false);

                    new Handler(getMainLooper()).postDelayed(() -> {
                        Toast.makeText(context, R.string.signup_success,
                                Toast.LENGTH_SHORT).show();

                        String userName = mBinding.layoutUsername.getEditText().getText().toString();
                        String password = mBinding.layoutPassword.getEditText().getText().toString();

                        mVM.setLoginUserName(userName);
                        mVM.setLoginPassword(password);

                        context.navigateTabLogin();
                    }, 1000);
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

    private boolean validateUserNameAndPassword(String userName, String password,
                                                String displayName, String rePassword) {
        if (userName.length() < 8) {
            mBinding.layoutUsername.setError(getText(R.string.username_error));
            mBinding.layoutUsername.getEditText().requestFocus();
            mBinding.layoutFullName.setErrorEnabled(false);
            mVM.setUserNameError(getText(R.string.username_error).toString());
            return false;
        }
        if (displayName.length() == 0) {
            mBinding.layoutFullName.setError(getText(R.string.display_name_error));
            mBinding.layoutFullName.getEditText().requestFocus();
            mBinding.layoutUsername.setErrorEnabled(false);
            mVM.setDisplayNameError(getText(R.string.display_name_error).toString());
            return false;
        }
        if (password.length() < 8) {
            mBinding.layoutPassword.setError(getText(R.string.password_error));
            mBinding.layoutPassword.getEditText().requestFocus();
            mBinding.layoutFullName.setErrorEnabled(false);
            mBinding.layoutUsername.setErrorEnabled(false);
            mVM.setPasswordError(getText(R.string.password_error).toString());
            return false;
        }
        if (rePassword.length() < 8 || !rePassword.equals(password)) {
            mBinding.layoutRePassword.setError(getText(R.string.re_password));
            mBinding.layoutRePassword.getEditText().requestFocus();
            mBinding.layoutPassword.setErrorEnabled(false);
            mBinding.layoutFullName.setErrorEnabled(false);
            mBinding.layoutUsername.setErrorEnabled(false);
            mVM.setRePasswordError(getText(R.string.re_password).toString());
            return false;
        }
        mBinding.layoutUsername.setErrorEnabled(false);
        mBinding.layoutPassword.setErrorEnabled(false);
        mBinding.layoutFullName.setErrorEnabled(false);
        mBinding.layoutRePassword.setErrorEnabled(false);
        return true;
    }

    private void resetState() {
        mVM.setUserName("");
        mVM.setDisplayName("");
        mVM.setPassword("");
        mVM.setRePassword("");
        mVM.setUserNameError("");
        mVM.setDisplayNameError("");
        mVM.setPasswordError("");
        mVM.setRePasswordError("");
        mBinding.layoutUsername.getEditText().requestFocus();
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
            mVM.signUp(user, context);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBinding.btnSignup.getId()) {
            String userName = mBinding.layoutUsername.getEditText().getText().toString();
            String password = mBinding.layoutPassword.getEditText().getText().toString();
            String displayName = mBinding.layoutFullName.getEditText().getText().toString();
            String rePassword = mBinding.layoutRePassword.getEditText().getText().toString();

            mVM.setUserName(userName);
            mVM.setPassword(password);
            mVM.setRePassword(rePassword);
            mVM.setDisplayName(displayName);

            signUp(userName, password, displayName, rePassword, context);
        }
    }
}

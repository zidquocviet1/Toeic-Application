package com.example.toeicapplication.utilities;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Learn more about this
public class MyActivityForResult<I, O> {

    public interface OnActivityResult<O> {
        /**
         * Called after receiving a result from the target activity
         */
        void onActivityResult(O result);
    }

    private final ActivityResultLauncher<I> launcher;
    @Nullable
    private OnActivityResult<O> callback;

    public void setOnActivityResult(@Nullable OnActivityResult<O> callback) {
        this.callback = callback;
    }

    /**
     * Launch activity, same as {@link ActivityResultLauncher#launch(Object)} except that it allows a callback
     * executed after receiving a result from the target activity.
     */
    public void mLaunch(I input, @Nullable OnActivityResult<O> callback) {
        if (callback != null) {
            this.callback = callback;
        }
        launcher.launch(input);
    }

    public void mLaunch(I input) {
        mLaunch(input, this.callback);
    }

    public static <I, O> MyActivityForResult<I, O> registerActivityForResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<I, O> contract,
            @Nullable OnActivityResult<O> callback){
        return new MyActivityForResult<>(caller, contract, callback);
    }

    public static <I, O> MyActivityForResult<I, O> registerActivityForResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<I, O> contract){
        return registerActivityForResult(caller, contract, null);
    }

    public static MyActivityForResult<Intent, ActivityResult> registerActivityForResult(
            @NonNull ActivityResultCaller caller){
        return registerActivityForResult(caller, new ActivityResultContracts.StartActivityForResult(), null);
    }

    private MyActivityForResult(@NonNull ActivityResultCaller caller,
                                 @NonNull ActivityResultContract<I, O> contract,
                                 @Nullable OnActivityResult<O> callback) {
        this.callback = callback;
        this.launcher = caller.registerForActivityResult(contract, this::callOnActivityResult);
    }

    private void callOnActivityResult(O o){
        if (callback != null){
            callback.onActivityResult(o);
        }
    }
}

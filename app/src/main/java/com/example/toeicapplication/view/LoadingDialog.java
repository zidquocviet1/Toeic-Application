package com.example.toeicapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.AnimationUtils;

import com.example.toeicapplication.R;

public class LoadingDialog {
    private static Dialog loadingDialog;

    public static void showLoadingDialog(Context context) {
        loadingDialog = new Dialog(context, R.style.LoadingDialog);

        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.show();
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.findViewById(R.id.loading_icon).startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate360));
    }

    public static void dismissDialog(){
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}

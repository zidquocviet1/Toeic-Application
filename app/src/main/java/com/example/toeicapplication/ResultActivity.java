package com.example.toeicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.toeicapplication.databinding.ActivityResultBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.Utils;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.viewmodels.ResultViewModel;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResultActivity extends BaseActivity<ResultViewModel, ActivityResultBinding>
        implements View.OnClickListener {
    private Result result;
    private Course course;
    private User user;

    @NonNull
    @NotNull
    @Override
    public Class<ResultViewModel> getViewModel() {
        return ResultViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    public ActivityResultBinding bindingInflater() {
        return ActivityResultBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();

        result = intent.getParcelableExtra("result");
        course = intent.getParcelableExtra("course");
        user = intent.getParcelableExtra("user");

        setupEvent();

        if (mVM != null){
            mVM.getResult().postValue(result);
        }
    }

    @Override
    protected void onDestroy() {
        // save the result to database and remote here
        if (mVM != null && result != null) mVM.add(result);
        super.onDestroy();
    }

    @Override
    public void setupObserver() {
        mVM.getResult().observe(this, result -> {
            if (result != null){
                showUI(result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.imgHome){
            this.finish();
        }else if (id == R.id.imgLeaderboard){
            Log.e("TAG", "cardView Leaderboard clicked");
        }else if (id == R.id.imgReviewAnswer){
            Log.e("TAG", "cardView ReviewAnswer clicked");
        }else if (id == R.id.imgShare){
            Log.e("TAG", "cardView Share clicked");
        }else if (id == R.id.imgTemp){
            Log.e("TAG", "cardView Temp clicked");
        }else if (id == R.id.imgTryAgain){
            if (course == null){
                Toast.makeText(this, getString(R.string.course_not_found), Toast.LENGTH_SHORT).show();
                return;
            }
            LoadingDialog.showLoadingDialog(this);

            Intent intent = new Intent(ResultActivity.this, ExamActivity.class);
            intent.putExtra("course", course);
            intent.putExtra("user", user);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                LoadingDialog.dismissDialog();
                startActivity(intent);
                this.finish();
            }, 1000);
        }
    }

    private void setupEvent(){
        mBinding.imgHome.setOnClickListener(this);
        mBinding.imgLeaderboard.setOnClickListener(this);
        mBinding.imgReviewAnswer.setOnClickListener(this);
        mBinding.imgShare.setOnClickListener(this);
        mBinding.imgTemp.setOnClickListener(this);
        mBinding.imgTryAgain.setOnClickListener(this);
    }

    private void showUI(Result result) {
        mBinding.txtScore.setText(result.getScore().toString());
        mBinding.txtComplete.setText(getString(R.string.complete_progress, result.getCompletion()));
        mBinding.txtCorrect.setText(getString(R.string.complete_question, result.getCorrect()));
        mBinding.txtWrong.setText(getString(R.string.complete_question, result.getWrong()));
        mBinding.txtTotal.setText(getString(R.string.complete_question, result.getTotalQuestion()));
        mBinding.txtDuration.setText(getString(R.string.duration, Utils.convertTime(result.getDuration())));
    }
}
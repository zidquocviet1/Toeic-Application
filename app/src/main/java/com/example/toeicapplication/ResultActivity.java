package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.toeicapplication.databinding.ActivityResultBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.Utils;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.viewmodels.ResultViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityResultBinding binding;
    private ResultViewModel resultVM;
    private Result result;
    private Course course;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        result = intent.getParcelableExtra("result");
        course = intent.getParcelableExtra("course");
        user = intent.getParcelableExtra("user");

        setupObserve();
        setupEvent();

        if (resultVM != null){
            resultVM.getResult().postValue(result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // save the result to database and remote here
        if (resultVM != null && result != null) resultVM.add(result);
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

    private void setupObserve(){
        resultVM = new ViewModelProvider(this).get(ResultViewModel.class);

        resultVM.getResult().observe(this, result -> {
            if (result != null){
                showUI(result);
            }
        });
    }

    private void setupEvent(){
        binding.imgHome.setOnClickListener(this);
        binding.imgLeaderboard.setOnClickListener(this);
        binding.imgReviewAnswer.setOnClickListener(this);
        binding.imgShare.setOnClickListener(this);
        binding.imgTemp.setOnClickListener(this);
        binding.imgTryAgain.setOnClickListener(this);
    }

    private void showUI(Result result) {
        binding.txtScore.setText(result.getScore().toString());
        binding.txtComplete.setText(getString(R.string.complete_progress, result.getCompletion()));
        binding.txtCorrect.setText(getString(R.string.complete_question, result.getCorrect()));
        binding.txtWrong.setText(getString(R.string.complete_question, result.getWrong()));
        binding.txtTotal.setText(getString(R.string.complete_question, result.getTotalQuestion()));
        binding.txtDuration.setText(getString(R.string.duration, Utils.convertTime(result.getDuration())));
    }
}
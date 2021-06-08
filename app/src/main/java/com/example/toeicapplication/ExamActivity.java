package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.toeicapplication.databinding.ActivityExamBinding;
import com.example.toeicapplication.model.Course;
import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.utilities.Utils;
import com.example.toeicapplication.view.custom.ChooseModeBottomDialogFragment;
import com.example.toeicapplication.viewmodels.ExamViewModel;
import com.example.toeicapplication.viewmodels.HomeViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExamActivity
        extends AppCompatActivity
        implements ChooseModeBottomDialogFragment.ChooseModeListener {
    private ActivityExamBinding binding;
    private ExamViewModel examVM;
    private List<Question> questions;
    private int TESTING_TIME = 2*60*60*1000;
    private boolean isCounting = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Course course = getIntent().getParcelableExtra("course");

        observeViewModel();
        loadListQuestion(course.getId());

        if (questions != null && !questions.isEmpty()){
            binding.txtTitle.setText(course.getName());
        }
    }

    private void observeViewModel() {
        examVM = new ViewModelProvider(this).get(ExamViewModel.class);

        examVM.getQuestions().observe(this, questionList -> {
            if (questionList != null && !questionList.isEmpty()){
                questions = new ArrayList<>(questionList);

                ChooseModeBottomDialogFragment bottomDialog
                        = ChooseModeBottomDialogFragment.newInstance();
                bottomDialog.setCancelable(false);
                bottomDialog.show(getSupportFragmentManager(), "Exam");
            }else{
                unavailableCourseDialog();
            }
        });
    }

    private void loadListQuestion(Long courseID){
        if (examVM != null){
            examVM.getListQuestionByCourseID(courseID);
        }
    }

    private void startCountingTime(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (TESTING_TIME > 0){
                        binding.pbTime.setProgress(TESTING_TIME);
                        binding.txtDisplayTime.setText(Utils.convertTime(TESTING_TIME));
                        TESTING_TIME -= 1000;
                    }else{
                        binding.pbTime.setProgress(0);
                        binding.txtDisplayTime.setText(Utils.convertTime(0));
//                        showResultDialog(false);
                    }
                });
            }
        }, 0, 1000);
    }

    private void unavailableCourseDialog(){
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle(R.string.account_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.course_not_found)
                .setNegativeButton(R.string.confirm, (dialog, which) -> {
                    dialog.dismiss();
                    this.finish();
                })
                .create().show();
    }

    private void startExam(){
        if (isCounting)
            startCountingTime();
        // ....
    }

    @Override
    public void onConfirm(boolean isCounting) {
        this.isCounting = isCounting;
        startExam();
    }

    @Override
    public void onCancel() {
        this.finish();
    }
}
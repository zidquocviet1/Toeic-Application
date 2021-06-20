package com.example.toeicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.toeicapplication.databinding.ActivityExamBinding;
import com.example.toeicapplication.model.Course;
import com.example.toeicapplication.model.Progress;
import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.utilities.Utils;
import com.example.toeicapplication.view.custom.ChooseModeBottomDialogFragment;
import com.example.toeicapplication.view.fragment.Part1Fragment;
import com.example.toeicapplication.view.fragment.Part2Fragment;
import com.example.toeicapplication.view.fragment.Part3Fragment;
import com.example.toeicapplication.view.fragment.Part5Fragment;
import com.example.toeicapplication.view.fragment.Part7Fragment;
import com.example.toeicapplication.viewmodels.ExamViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExamActivity
        extends AppCompatActivity
        implements ChooseModeBottomDialogFragment.ChooseModeListener, View.OnClickListener, MediaPlayer.OnPreparedListener {
    private ActivityExamBinding binding;
    private ExamViewModel examVM;
    private MediaPlayer mediaPlayer;
    private Progress progress;

    private List<Question> questions;
    private Map<Integer, String> progressQuestion;
    private OnConfirmAnswer callback;

    private boolean isCounting = true;

    private int testTime = 2 * 60 * 60 * 1000;
    private int currentPart = -1;
    private int currentQuestion = 1;
    private int currentAudio;

    public interface OnConfirmAnswer{
        void onConfirm();
    }

    public void setOnConfirmAnswer(OnConfirmAnswer callback){
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Course course = getIntent().getParcelableExtra("course");

        observeViewModel();
        loadListQuestion(course.getId());
        initMediaPlayer();

        binding.txtTitle.setText(course.getName());
        binding.btnClick.setOnClickListener(this);
        binding.btnClick.setEnabled(false);
    }

    private void observeViewModel() {
        examVM = new ViewModelProvider(this).get(ExamViewModel.class);

        examVM.getQuestions().observe(this, questionList -> {
            if (questionList != null && !questionList.isEmpty()) {
                questions = new ArrayList<>(questionList);

                if (checkProgress()) {
                    restartProgressDialog();
                } else {
                    bottomSheetDialog();
                }
            } else {
                unavailableCourseDialog();
            }
        });

        examVM.getProgress().observe(this, progress -> {
            if (progress != null) {
                this.progress = progress;
            }
        });
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setOnPreparedListener(this);
    }

    private void loadListQuestion(Long courseID) {
        if (examVM != null) {
            examVM.getProgressByCourseID(courseID);
            examVM.getListQuestionByCourseID(courseID);
        }
    }

    private void startExam() {
        if (isCounting) {
            binding.layoutTime.setVisibility(View.VISIBLE);
            binding.pbTime.setVisibility(View.VISIBLE);
            startCountingTime();
        }

        Question question = questions.get(currentQuestion - 1);
        openFragment(question);
    }

    private void openFragment(Question question) {
        this.currentPart = question.getPart();

        Fragment fragment = null;

        switch (this.currentPart) {
            case 1:
                fragment = Part1Fragment.newInstance(this.currentQuestion, question);
                break;
            case 2:
                fragment = Part2Fragment.newInstance(this.currentQuestion, question);
                break;
            case 3:
            case 4:
                fragment = Part3Fragment.newInstance("", "");
                break;
            case 5:
            case 6:
                fragment = Part5Fragment.newInstance("", "");
                break;
            case 7:
                fragment = Part7Fragment.newInstance("", "");
                break;
            default:
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.framelayout.getId(), fragment)
                    .commit();
        }

    }

    private void startCountingTime() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (testTime > 0) {
                        binding.pbTime.setProgress(testTime);
                        binding.txtDisplayTime.setText(Utils.convertTime(testTime));
                        testTime -= 1000;
                    } else {
                        binding.pbTime.setProgress(0);
                        binding.txtDisplayTime.setText(Utils.convertTime(0));
//                        showResultDialog(false);
                    }
                });
            }
        }, 0, 1000);
    }

    private boolean checkProgress() {
        return progress != null && progress.getRemainTime() > 0;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.btnClick.getId()) {
            // send a callback to announce answers are selected
            if (this.callback != null){
                callback.onConfirm();
            }

            // refresh fragment with a new question
            Question question = null;

            switch (this.currentPart){
                case 1:
                    question = questions.get(this.currentQuestion++);
                    break;
                case 2:
                    this.currentQuestion += 3;
                    question = questions.get(this.currentQuestion);
                    break;
            }
            if (question != null)
                openFragment(question);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
//        saveProgress(false);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            currentAudio = mediaPlayer.getCurrentPosition();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(currentAudio);
            mediaPlayer.start();
        }
        super.onRestart();
    }

    private void unavailableCourseDialog() {
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

    private void restartProgressDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.account_title)
                .setMessage(R.string.progress_dialog_message)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                    bottomSheetDialog();
                })
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    isCounting = progress.isCounting();
                    testTime = progress.getRemainTime().intValue();
                    progressQuestion = new HashMap<>(progress.getQuestions());

                    startExam();
                }).create().show();
    }

    private void bottomSheetDialog() {
        ChooseModeBottomDialogFragment bottomDialog
                = ChooseModeBottomDialogFragment.newInstance();
        bottomDialog.setCancelable(false);
        bottomDialog.show(getSupportFragmentManager(), "Exam");
    }

    public void changeButtonState(boolean isEnable) {
        binding.btnClick.setEnabled(isEnable);
    }

    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }

    public void startListening(String path) {
        try {
            AssetFileDescriptor afd = getAssets().openFd(path);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.example.toeicapplication;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.toeicapplication.databinding.ActivityExamBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Progress;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.Utils;
import com.example.toeicapplication.view.custom.ChooseModeBottomDialogFragment;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.view.fragment.Part1Fragment;
import com.example.toeicapplication.view.fragment.Part2Fragment;
import com.example.toeicapplication.view.fragment.Part5Fragment;
import com.example.toeicapplication.view.fragment.Part7Fragment;
import com.example.toeicapplication.viewmodels.ExamViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExamActivity
        extends BaseActivity<ExamViewModel, ActivityExamBinding>
        implements ChooseModeBottomDialogFragment.ChooseModeListener, View.OnClickListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private Progress progress;
    private Course course;
    private User user;

    private List<Question> questions;
    private Map<Integer, String> progressQuestion;
    private OnConfirmAnswer callback;

    private boolean isCounting = true;

    private static final int FIX_TIME = 2 * 60 * 60 * 1000;
    private int testTime = 2 * 60 * 60 * 1000;
    private int currentPart = -1;
    private int currentQuestion = 1;
    private int currentAudio;

    public interface OnConfirmAnswer {
        void onConfirm();
    }

    public void setOnConfirmAnswer(OnConfirmAnswer callback) {
        this.callback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public Class<ExamViewModel> getViewModel() {
        return ExamViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_exam;
    }

    @Override
    public ActivityExamBinding bindingInflater() {
        return ActivityExamBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        course = intent.getParcelableExtra("course");
        user = intent.getParcelableExtra("user");

        mVM.loadListQuestion(course.getId());
        initMediaPlayer();

        mBinding.txtTitle.setText(course.getName());
        mBinding.btnClick.setOnClickListener(this);
        mBinding.btnFinish.setOnClickListener(this);
        mBinding.btnClick.setEnabled(false);
        mBinding.btnFinish.setEnabled(false);
    }

    @Override
    public void setupObserver() {
        if (mVM != null){
            mVM.getProgress().observe(this, progress -> {
                if (progress != null) {
                    this.progress = progress;
                }
            });

            mVM.getQuestions().observe(this, questionList -> {
                if (questionList != null && !questionList.isEmpty()) {
                    questions = new ArrayList<>(questionList);
                    mBinding.txtTotal.setText(getString(R.string.total_question, questions.size()));

                    if (checkProgress()) {
                        restartProgressDialog();
                    } else {
                        bottomSheetDialog();
                    }
                } else {
                    unavailableCourseDialog();
                }
            });
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setOnPreparedListener(this);
    }

    // start exam executed only one time
    private void startExam() {
        mBinding.btnFinish.setEnabled(true);
        setupUIWithCountDown(isCounting);

        Question question = questions.get(currentQuestion - 1);
        ArrayList<Question> listQuestions = new ArrayList<>(getListQuestion(question));

        openFragment(listQuestions, null);
    }

    private void restartExam() {
        mBinding.btnFinish.setEnabled(true);
        isCounting = progress.isCounting();
        testTime = progress.getRemainTime().intValue();
        progressQuestion = new HashMap<>(progress.getQuestions());

        mVM.postSelectedQuestion(progressQuestion);

        setupUIWithCountDown(isCounting);

        int progressQuestionIndex = progressQuestion.size();

        Question question = questions.get(progressQuestionIndex);
        ArrayList<Question> questionList = getListQuestion(question);
        HashMap<Integer, String> progressAnswer = new HashMap<>();

        /*
            In here we have 2 cases:
                - First one, the index of question (progressQuestionIndex) is correct
                with the first index of bunch of question. Like (11, 12, 13), the current index is 11.
                So when we open the fragment of part 2 it will show the order of question are correctly.

                - Second one, the index of question at this time is not expected. As you see the example above,
                the current index is 12, the user was stopped taking the exam and select the question 11 at that time.
                So we have to get the punch of question and then update the current question. After that, make sure
                set the result which is the user has selected.
        */

        if (questions.indexOf(questionList.get(0)) != progressQuestionIndex) {
            this.currentQuestion = questions.indexOf(questionList.get(0)) + 1;
        } else {
            this.currentQuestion = progressQuestionIndex + 1;
        }

        for (Question q : questionList) {
            int index = questions.indexOf(q);

            if (progressQuestion.containsKey(index + 1)) {
                progressAnswer.put(index + 1, progressQuestion.get(index + 1));
            }
        }
        openFragment(questionList, progressAnswer);
    }

    private void openFragment(ArrayList<Question> listQuestion, @Nullable HashMap<Integer, String> progressAnswer) {
        Fragment fragment = null;

        switch (this.currentPart) {
            case 1:
                fragment = Part1Fragment.newInstance(this.currentQuestion, listQuestion.get(0));
                break;
            case 2:
            case 3:
            case 4:
                fragment = Part2Fragment.newInstance(this.currentQuestion, listQuestion, this.currentPart, progressAnswer);
                break;
            case 5:
            case 6:
                fragment = Part5Fragment.newInstance(this.currentQuestion, listQuestion);
                break;
            case 7:
                fragment = Part7Fragment.newInstance(this.currentQuestion, listQuestion, progressAnswer);
                break;
            default:
                break;
        }
        this.currentQuestion += listQuestion.size();

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(mBinding.framelayout.getId(), fragment)
                    .commit();
        }
    }

    private ArrayList<Question> getListQuestion(Question question) {
        ArrayList<Question> listQuestion = new ArrayList<>();
        this.currentPart = question.getPart();

        switch (this.currentPart) {
            case 1:
                listQuestion.add(question);
                break;
            case 2:
            case 3:
            case 4:
                listQuestion.addAll(
                        questions
                                .stream()
                                .filter(q -> q.getAudioFile().equals(question.getAudioFile())
                                        && q.getCourseID() == question.getCourseID() && q.getPart() == question.getPart())
                                .collect(Collectors.toCollection(ArrayList::new))
                );
                break;
            case 5:
            case 6:
                for (int i = 0; i < 4; i++) {
                    Question q = questions.get(this.questions.indexOf(question) + i);

                    if (q.getPart() == 5 || q.getPart() == 6) {
                        listQuestion.add(q);
                    }
                }
                break;
            case 7:
                listQuestion.addAll(
                        questions
                                .stream()
                                .filter(q -> q.getDescription() != null && q.getDescription().equals(question.getDescription())
                                        && q.getCourseID() == question.getCourseID() && q.getPart() == question.getPart())
                                .collect(Collectors.toCollection(ArrayList::new))
                );
                break;
        }

        return listQuestion;
    }

    private void setupUIWithCountDown(boolean isCounting) {
        if (isCounting) {
            mBinding.layoutTime.setVisibility(View.VISIBLE);
            mBinding.pbTime.setVisibility(View.VISIBLE);
            startCountingTime();
        }
    }

    private void startCountingTime() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (mBinding != null) {
                        if (testTime > 0) {
                            mBinding.pbTime.setProgress(testTime);
                            mBinding.txtDisplayTime.setText(Utils.convertTime(testTime));
                            testTime -= 1000;
                        } else {
                            mBinding.pbTime.setProgress(0);
                            mBinding.txtDisplayTime.setText(Utils.convertTime(0));
                            timer.cancel();
                            showResult(false);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private boolean checkProgress() {
        // the progress has been checked complete or not.
        // So we don't need to check the size of answer
        return progress != null
                && progress.getRemainTime() > 0
                && progress.getQuestions() != null
                && progress.getQuestions().size() < this.questions.size();
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

        if (id == mBinding.btnClick.getId()) {
            // send a callback to announce answers are selected
            if (this.callback != null) {
                callback.onConfirm();
            }

            // show result here
            if (this.currentQuestion >= this.questions.size()) {
                showResult(false);
                return;
            }

            // refresh fragment with a new question
            Question question = this.questions.get(this.currentQuestion - 1);
            openFragment(getListQuestion(question), null);
        } else if (id == mBinding.btnFinish.getId()) {
            earlyFinishExamDialog();
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

    @Override
    public void onBackPressed() {
        stopExamDialog();
    }

    private void addProgress() {
        Progress progress = new Progress(this.progress != null ? this.progress.getId() : null,
                this.course.getId(),
                (long) this.testTime,
                mVM.getSelectedQuestion().getValue(),
                isCounting);
        mVM.add(progress);
    }

    private void showResult(boolean isEarlyStop) {
        if (!isEarlyStop)
            addProgress();
        else mVM.delete(course.getId());

        LoadingDialog.showLoadingDialog(this);

        Result result = null;
        Map<String, Integer> resultMap = mVM.calculateAnswer();
        Long userId = user != null ? user.getId() : null;

        if (resultMap != null) {
            result = new Result(null,
                    userId,
                    this.course.getId(),
                    resultMap.get("totalScore"),
                    resultMap.get("reading"),
                    resultMap.get("listening"),
                    resultMap.get("correct"),
                    resultMap.get("wrong"),
                    resultMap.get("totalQuestion"),
                    resultMap.get("completion"),
                    testTime <= 0 ? FIX_TIME : FIX_TIME - testTime,
                    LocalDateTime.now());
        }

        Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
        intent.putExtra("result", result);
        intent.putExtra("user", user);
        intent.putExtra("course", course);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LoadingDialog.dismissDialog();
            startActivity(intent);
            this.finish();
        }, 1000);
    }

    private void stopExamDialog() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle(R.string.account_title)
                .setMessage(R.string.exit_testing_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.dismiss();

                    addProgress();

                    this.finish();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void earlyFinishExamDialog() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle(R.string.account_title)
                .setMessage(R.string.early_finish)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.dismiss();

                    showResult(true);
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
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
                    restartExam();
                }).create().show();
    }

    private void bottomSheetDialog() {
        ChooseModeBottomDialogFragment bottomDialog
                = ChooseModeBottomDialogFragment.newInstance();
        bottomDialog.setCancelable(false);
        bottomDialog.show(getSupportFragmentManager(), "Exam");
    }

    public void changeButtonState(boolean isEnable) {
        mBinding.btnClick.setEnabled(isEnable);
    }

    public MediaPlayer getMediaPlayer() {
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

    public void setQuestionTitle(String question) {
        mBinding.questionId.setText(question);
    }
}
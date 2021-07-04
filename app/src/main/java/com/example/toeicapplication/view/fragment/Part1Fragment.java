package com.example.toeicapplication.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.toeicapplication.ExamActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.FragmentPart1Binding;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.viewmodels.ExamViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Part1Fragment extends BaseFragment<ExamViewModel, FragmentPart1Binding>
        implements View.OnClickListener, ExamActivity.OnConfirmAnswer {
    private ExamActivity context;
    private Question question;

    private static final String NUM_QUESTION = "numQuestion";
    private static final String QUESTION = "question";

    private Drawable answerNormalState;
    private Drawable answerSelectedState;
    private List<TextView> widgets;

    private int numQuestion;
    private String answer;

    public Part1Fragment() {
        // Required empty public constructor
    }

    public static Part1Fragment newInstance(int numQuestion, Question question) {
        Part1Fragment fragment = new Part1Fragment();
        Bundle args = new Bundle();
        args.putInt(NUM_QUESTION, numQuestion);
        args.putParcelable(QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numQuestion = getArguments().getInt(NUM_QUESTION);
            question = getArguments().getParcelable(QUESTION);
        }
    }

    @Override
    public FragmentPart1Binding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentPart1Binding.inflate(inflater, container, attachToParent);
    }

    @Override
    public Class<ExamViewModel> getViewModel() {
        return ExamViewModel.class;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return requireActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (ExamActivity) getActivity();

        if (context != null) {
            context.setOnConfirmAnswer(this);
            context.changeButtonState(false);
            answerNormalState = ContextCompat.getDrawable(context, R.drawable.answer_1);
            answerSelectedState = ContextCompat.getDrawable(context, R.drawable.answer_selected);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupEvent();

        if (question != null){
            showQuestion(question);
        }

        widgets = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            widgets.addAll(List.of(mBinding.txtA, mBinding.txtB, mBinding.txtC, mBinding.txtD));
        }else{
            widgets.add(mBinding.txtA);
            widgets.add(mBinding.txtB);
            widgets.add(mBinding.txtC);
            widgets.add(mBinding.txtD);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        widgets.forEach(txt -> {
            if (txt.getId() == id){
                txt.setBackground(answerSelectedState);
                answer = txt.getText().toString();
            }else{
                txt.setBackground(answerNormalState);
            }
        });
        context.changeButtonState(true);
    }

    @Override
    public void onConfirm() {
        mVM.postSelectedQuestion(numQuestion, answer);
    }

    private void setupEvent() {
        mBinding.txtA.setOnClickListener(this);
        mBinding.txtB.setOnClickListener(this);
        mBinding.txtC.setOnClickListener(this);
        mBinding.txtD.setOnClickListener(this);
    }

    private void showQuestion(Question question) {
        if (context.getMediaPlayer() != null && context.getMediaPlayer().isPlaying()) {
            context.getMediaPlayer().stop();
        }
        try {
            if (question.getDescription() != null) {
                InputStream is = context.getAssets().open(question.getDescription());
                Drawable drawable = Drawable.createFromStream(is, null);
                mBinding.image.setImageDrawable(drawable);
                context.setQuestionTitle(String.valueOf(numQuestion));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mBinding.txtA.setText(question.getQuestionA());
        mBinding.txtB.setText(question.getQuestionB());
        mBinding.txtC.setText(question.getQuestionC());
        mBinding.txtD.setText(question.getQuestionD());

        if (context.getMediaPlayer() != null) {
            context.getMediaPlayer().reset();
            context.startListening(question.getAudioFile());
        }
    }
}
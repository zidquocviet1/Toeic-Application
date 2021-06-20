package com.example.toeicapplication.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toeicapplication.ExamActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.FragmentPart1Binding;
import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.viewmodels.ExamViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Part1Fragment extends Fragment implements View.OnClickListener, ExamActivity.OnConfirmAnswer {
    private FragmentPart1Binding binding;
    private ExamActivity context;
    private ExamViewModel examVM;
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPart1Binding.inflate(inflater, container, false);
        context = (ExamActivity) getActivity();

        if (context != null) {
            context.setOnConfirmAnswer(this);
            context.changeButtonState(false);
            answerNormalState = ContextCompat.getDrawable(context, R.drawable.answer_1);
            answerSelectedState = ContextCompat.getDrawable(context, R.drawable.answer_selected);
        }

        setupEvent();
        examVM = new ViewModelProvider(requireActivity()).get(ExamViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (question != null){
            showQuestion(question);
        }

        widgets = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            widgets.addAll(List.of(binding.txtA, binding.txtB, binding.txtC, binding.txtD));
        }else{
            widgets.add(binding.txtA);
            widgets.add(binding.txtB);
            widgets.add(binding.txtC);
            widgets.add(binding.txtD);
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
        examVM.postSelectedQuestion(numQuestion, answer);
    }

    private void setupEvent() {
        binding.txtA.setOnClickListener(this);
        binding.txtB.setOnClickListener(this);
        binding.txtC.setOnClickListener(this);
        binding.txtD.setOnClickListener(this);
    }

    private void showQuestion(Question question) {
        if (context.getMediaPlayer() != null && context.getMediaPlayer().isPlaying()) {
            context.getMediaPlayer().stop();
        }
        try {
            if (question.getDescription() != null) {
                InputStream is = context.getAssets().open(question.getDescription());
                Drawable drawable = Drawable.createFromStream(is, null);
                binding.image.setImageDrawable(drawable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.txtA.setText(question.getQuestionA());
        binding.txtB.setText(question.getQuestionB());
        binding.txtC.setText(question.getQuestionC());
        binding.txtD.setText(question.getQuestionD());

        if (context.getMediaPlayer() != null) {
            context.getMediaPlayer().reset();
            context.startListening(question.getAudioFile());
        }
    }
}
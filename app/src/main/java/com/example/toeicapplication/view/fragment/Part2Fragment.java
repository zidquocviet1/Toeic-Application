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
import com.example.toeicapplication.databinding.FragmentPart2Binding;
import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.viewmodels.ExamViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Part2Fragment extends Fragment
        implements ExamActivity.OnConfirmAnswer, View.OnClickListener {

    private FragmentPart2Binding binding;
    private ExamViewModel examVM;
    private ExamActivity context;
    private Question question;

    private static final String NUM_QUESTION = "numQuestion";
    private static final String QUESTION = "question";

    private Drawable answerNormalState;
    private Drawable answerSelectedState;
    private List<TextView> widgets;
    private Map<Integer, String> answer;

    private int numQuestion;


    public Part2Fragment() {
        // Required empty public constructor
    }

    public static Part2Fragment newInstance(int numQuestion,
                                            Question question) {
        Part2Fragment fragment = new Part2Fragment();
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
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPart2Binding.inflate(inflater,
                container, false);
        context = (ExamActivity) getActivity();

        if (context != null) {
            context.setOnConfirmAnswer(this);
            context.changeButtonState(false);
            answerNormalState = ContextCompat.getDrawable(context,
                    R.drawable.answer_1);
            answerSelectedState = ContextCompat.getDrawable(context,
                    R.drawable.answer_selected);
        }

        setupEvent();
        examVM = new ViewModelProvider(requireActivity()).
                get(ExamViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (question != null){
            showQuestion(question);
        }

        widgets = new ArrayList<>();
        answer = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            widgets.addAll(List.of(binding.txt1A, binding.txt1B, binding.txt1C,
                    binding.txt2A, binding.txt2B, binding.txt2C,
                    binding.txt3A, binding.txt3B, binding.txt3C));
        }else{
            widgets.add(binding.txt1A);
            widgets.add(binding.txt1B);
            widgets.add(binding.txt1C);

            widgets.add(binding.txt2A);
            widgets.add(binding.txt2B);
            widgets.add(binding.txt2C);

            widgets.add(binding.txt3A);
            widgets.add(binding.txt3B);
            widgets.add(binding.txt3C);
        }
    }

    @Override
    public void onConfirm() {
        examVM.postSelectedQuestion(answer);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Integer tag = (Integer) v.getTag();

        widgets.forEach(txt -> {
            if (txt.getId() == id && txt.getTag() == tag){
                txt.setBackground(answerSelectedState);
                answer.put(tag, txt.getText().toString());
            }else if (txt.getTag() == tag && txt.getId() != id){
                txt.setBackground(answerNormalState);
            }
        });
        if (answer.size() == 3)
            context.changeButtonState(true);
    }

    private void setupEvent() {
        binding.txt1A.setOnClickListener(this);
        binding.txt1B.setOnClickListener(this);
        binding.txt1C.setOnClickListener(this);

        binding.txt2A.setOnClickListener(this);
        binding.txt2B.setOnClickListener(this);
        binding.txt2C.setOnClickListener(this);

        binding.txt3A.setOnClickListener(this);
        binding.txt3B.setOnClickListener(this);
        binding.txt3C.setOnClickListener(this);
    }

    private void showQuestion(Question question) {
        if (context.getMediaPlayer() != null && context.getMediaPlayer().isPlaying()) {
            context.getMediaPlayer().stop();
        }

        context.setQuestionTitle(this.numQuestion + "-" + (this.numQuestion + 2));

        binding.txt1A.setText(question.getQuestionA());
        binding.txt1B.setText(question.getQuestionB());
        binding.txt1C.setText(question.getQuestionC());

        binding.txt1A.setTag(this.numQuestion);
        binding.txt1B.setTag(this.numQuestion);
        binding.txt1C.setTag(this.numQuestion);
        binding.question1.setText(context.getString(R.string.number_question, this.numQuestion++));

        binding.txt2A.setTag(this.numQuestion);
        binding.txt2B.setTag(this.numQuestion);
        binding.txt2C.setTag(this.numQuestion);
        binding.question2.setText(context.getString(R.string.number_question, this.numQuestion++));

        binding.txt3A.setTag(this.numQuestion);
        binding.txt3B.setTag(this.numQuestion);
        binding.txt3C.setTag(this.numQuestion);
        binding.question3.setText(context.getString(R.string.number_question, this.numQuestion));

        if (context.getMediaPlayer() != null) {
            context.getMediaPlayer().reset();
            context.startListening(question.getAudioFile());
        }
    }
}
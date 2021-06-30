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
import com.example.toeicapplication.databinding.FragmentPart5Binding;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.viewmodels.ExamViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part5Fragment extends Fragment implements ExamActivity.OnConfirmAnswer, View.OnClickListener {

    private FragmentPart5Binding binding;
    private ExamViewModel examVM;
    private ExamActivity context;

    private static final String NUM_QUESTION = "numQuestion";
    private static final String QUESTION = "question";

    private Drawable answerNormalState;
    private Drawable answerSelectedState;
    private List<TextView> questionTitle;
    private List<ViewGroup> layouts;
    private final List<Question> questions = new ArrayList<>();
    private Map<Integer, String> answer;
    private Map<String, TextView> questionContent;

    private int numQuestion;

    public Part5Fragment() {
        // Required empty public constructor
    }

    public static Part5Fragment newInstance(int numQuestion,
                                            ArrayList<Question> question) {
        Part5Fragment fragment = new Part5Fragment();
        Bundle args = new Bundle();
        args.putInt(NUM_QUESTION, numQuestion);
        args.putParcelableArrayList(QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numQuestion = getArguments().getInt(NUM_QUESTION);
            questions.addAll(getArguments().getParcelableArrayList(QUESTION));
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPart5Binding.inflate(inflater,
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

        questionTitle = new ArrayList<>();
        layouts = new ArrayList<>();
        answer = new HashMap<>();
        questionContent = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            questionTitle.addAll(List.of(binding.question1, binding.question2, binding.question3, binding.question4));
            layouts.addAll(List.of(binding.layout1, binding.layout2, binding.layout3, binding.layout4));
        }else{
            questionTitle.add(binding.question1);
            questionTitle.add(binding.question2);
            questionTitle.add(binding.question3);
            questionTitle.add(binding.question4);

            layouts.add(binding.layout1);
            layouts.add(binding.layout2);
            layouts.add(binding.layout3);
            layouts.add(binding.layout4);
        }

        questionContent.put("1A", binding.txt1A);
        questionContent.put("1B", binding.txt1B);
        questionContent.put("1C", binding.txt1C);
        questionContent.put("1D", binding.txt1D);

        questionContent.put("2A", binding.txt2A);
        questionContent.put("2B", binding.txt2B);
        questionContent.put("2C", binding.txt2C);
        questionContent.put("2D", binding.txt2D);

        questionContent.put("3A", binding.txt3A);
        questionContent.put("3B", binding.txt3B);
        questionContent.put("3C", binding.txt3C);
        questionContent.put("3D", binding.txt3D);

        questionContent.put("4A", binding.txt4A);
        questionContent.put("4B", binding.txt4B);
        questionContent.put("4C", binding.txt4C);
        questionContent.put("4D", binding.txt4D);
        showQuestion(questions);
    }

    @Override
    public void onConfirm() {
        examVM.postSelectedQuestion(answer);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Integer tag = (Integer) v.getTag();

        questionContent.entrySet().forEach(item -> {
            TextView txt = item.getValue();
            // with Integer == is only works with the value between -128 to 127
            if (txt.getId() == id && txt.getTag().equals(tag)){
                txt.setBackground(answerSelectedState);
                answer.put(tag, txt.getText().toString());
            }else if (txt.getTag().equals(tag) && txt.getId() != id){
                txt.setBackground(answerNormalState);
            }
        });
        if (answer.size() == this.questions.size())
            context.changeButtonState(true);
    }

    private void showQuestion(List<Question> questions) {
        if (context.getMediaPlayer() != null && context.getMediaPlayer().isPlaying()) {
            context.getMediaPlayer().stop();
        }

        context.setQuestionTitle(this.numQuestion + "-" + (this.numQuestion + this.questions.size() - 1));

        for (int i = 0; i < questions.size(); i++){
            layouts.get(i).setVisibility(View.VISIBLE);
            // set question title
            Question question = questions.get(i);
            TextView txtTitle = questionTitle.get(i);
            TextView txtA = questionContent.get((i+1)+"A");
            TextView txtB = questionContent.get((i+1)+"B");
            TextView txtC = questionContent.get((i+1)+"C");
            TextView txtD = questionContent.get((i+1)+"D");

            txtTitle.setText(question.getQuestion());
            txtA.setText(question.getQuestionA());
            txtB.setText(question.getQuestionB());
            txtC.setText(question.getQuestionC());
            txtD.setText(question.getQuestionD());
        }
    }

    private void setupEvent() {
        binding.txt1A.setOnClickListener(this);
        binding.txt1B.setOnClickListener(this);
        binding.txt1C.setOnClickListener(this);
        binding.txt1D.setOnClickListener(this);

        binding.txt2A.setOnClickListener(this);
        binding.txt2B.setOnClickListener(this);
        binding.txt2C.setOnClickListener(this);
        binding.txt2D.setOnClickListener(this);

        binding.txt3A.setOnClickListener(this);
        binding.txt3B.setOnClickListener(this);
        binding.txt3C.setOnClickListener(this);
        binding.txt3D.setOnClickListener(this);

        binding.txt4A.setOnClickListener(this);
        binding.txt4B.setOnClickListener(this);
        binding.txt4C.setOnClickListener(this);
        binding.txt4D.setOnClickListener(this);

        // set tag for the purpose of onClick
        binding.txt1A.setTag(this.numQuestion);
        binding.txt1B.setTag(this.numQuestion);
        binding.txt1C.setTag(this.numQuestion);
        binding.txt1D.setTag(this.numQuestion);

        binding.txt2A.setTag(this.numQuestion + 1);
        binding.txt2B.setTag(this.numQuestion + 1);
        binding.txt2C.setTag(this.numQuestion + 1);
        binding.txt2D.setTag(this.numQuestion + 1);

        binding.txt3A.setTag(this.numQuestion + 2);
        binding.txt3B.setTag(this.numQuestion + 2);
        binding.txt3C.setTag(this.numQuestion + 2);
        binding.txt3D.setTag(this.numQuestion + 2);

        binding.txt4A.setTag(this.numQuestion + 3);
        binding.txt4B.setTag(this.numQuestion + 3);
        binding.txt4C.setTag(this.numQuestion + 3);
        binding.txt4D.setTag(this.numQuestion + 3);
    }
}
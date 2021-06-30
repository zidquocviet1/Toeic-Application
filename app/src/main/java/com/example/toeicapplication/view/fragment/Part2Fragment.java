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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.toeicapplication.ExamActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.FragmentPart2Binding;
import com.example.toeicapplication.model.entity.Question;
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

    private static final String NUM_QUESTION = "numQuestion";
    private static final String QUESTION = "question";
    private static final String PART = "part";
    private static final String PROGRESS_ANSWER = "progressAnswer";

    private Drawable answerNormalState;
    private Drawable answerSelectedState;
    private List<TextView> questionTitle;
    private Map<Integer, String> answer;
    private Map<String, TextView> questionContent;
    private final List<Question> questions = new ArrayList<>();
    private final Map<Integer, String> progressAnswer = new HashMap<>();

    private int numQuestion;
    private int part;

    public Part2Fragment() {
        // Required empty public constructor
    }

    public static Part2Fragment newInstance(int numQuestion,
                                            ArrayList<Question> question, int part,
                                            @Nullable HashMap<Integer, String> progressAnswer) {
        Part2Fragment fragment = new Part2Fragment();
        Bundle args = new Bundle();
        args.putInt(NUM_QUESTION, numQuestion);
        args.putParcelableArrayList(QUESTION, question);
        args.putInt(PART, part);
        args.putSerializable(PROGRESS_ANSWER, progressAnswer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numQuestion = getArguments().getInt(NUM_QUESTION);
            questions.addAll(getArguments().getParcelableArrayList(QUESTION));
            part = getArguments().getInt(PART);

            HashMap<Integer, String> hashMap = (HashMap<Integer, String>) getArguments().getSerializable(PROGRESS_ANSWER);
            if (hashMap != null){
                progressAnswer.putAll(hashMap);
            }
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

        questionTitle = new ArrayList<>();
        answer = new HashMap<>();
        questionContent = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            questionTitle.addAll(List.of(binding.question1, binding.question2, binding.question3));
        }else{
            questionTitle.add(binding.question1);
            questionTitle.add(binding.question2);
            questionTitle.add(binding.question3);
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
            if (txt.getId() == id && txt.getTag().equals(tag)){
                txt.setBackground(answerSelectedState);
                answer.put(tag, txt.getText().toString());
            }else if (txt.getTag().equals(tag) && txt.getId() != id){
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
        binding.txt1D.setOnClickListener(this);

        binding.txt2A.setOnClickListener(this);
        binding.txt2B.setOnClickListener(this);
        binding.txt2C.setOnClickListener(this);
        binding.txt2D.setOnClickListener(this);

        binding.txt3A.setOnClickListener(this);
        binding.txt3B.setOnClickListener(this);
        binding.txt3C.setOnClickListener(this);
        binding.txt3D.setOnClickListener(this);
    }

    private void showQuestion(List<Question> questions) {
        if (context.getMediaPlayer() != null && context.getMediaPlayer().isPlaying()) {
            context.getMediaPlayer().stop();
        }

        context.setQuestionTitle(this.numQuestion + "-" + (this.numQuestion + this.questions.size() - 1));

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

        if (part == 2){
            binding.question1.setText(context.getString(R.string.number_question, this.numQuestion));
            binding.question2.setText(context.getString(R.string.number_question, this.numQuestion + 1));
            binding.question3.setText(context.getString(R.string.number_question, this.numQuestion + 2));
        }else if (part == 3 || part == 4){
            binding.txt1D.setVisibility(View.VISIBLE);
            binding.txt2D.setVisibility(View.VISIBLE);
            binding.txt3D.setVisibility(View.VISIBLE);

            for (int i = 0; i < questions.size(); i++){
                // set question title
                Question question = questions.get(i);
                TextView txt = questionTitle.get(i);

                txt.setText(question.getQuestion());

                questionContent.get((i+1)+"A").setText(question.getQuestionA());
                questionContent.get((i+1)+"B").setText(question.getQuestionB());
                questionContent.get((i+1)+"C").setText(question.getQuestionC());
                questionContent.get((i+1)+"D").setText(question.getQuestionD());
            }
        }

        // show the answer are choice before
        questionContent.entrySet().forEach(item -> {
            TextView txt = item.getValue();
            Integer tag = (Integer) txt.getTag();

            if (txt.getText().toString().equals(progressAnswer.get(tag))) {
                txt.setBackground(answerSelectedState);
                answer.put(tag, txt.getText().toString());
            }
        });

        if (context.getMediaPlayer() != null) {
            context.getMediaPlayer().reset();
            context.startListening(questions.get(0).getAudioFile());
        }
    }
}
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
public class Part2Fragment extends BaseFragment<ExamViewModel, FragmentPart2Binding>
        implements ExamActivity.OnConfirmAnswer, View.OnClickListener {
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
    public FragmentPart2Binding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentPart2Binding.inflate(inflater, container, attachToParent);
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
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        context = (ExamActivity) getActivity();

        if (context != null) {
            context.setOnConfirmAnswer(this);
            context.changeButtonState(false);
            answerNormalState = ContextCompat.getDrawable(context,
                    R.drawable.answer_1);
            answerSelectedState = ContextCompat.getDrawable(context,
                    R.drawable.answer_selected);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupEvent();

        questionTitle = new ArrayList<>();
        answer = new HashMap<>();
        questionContent = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            questionTitle.addAll(List.of(mBinding.question1, mBinding.question2, mBinding.question3));
        }else{
            questionTitle.add(mBinding.question1);
            questionTitle.add(mBinding.question2);
            questionTitle.add(mBinding.question3);
        }

        questionContent.put("1A", mBinding.txt1A);
        questionContent.put("1B", mBinding.txt1B);
        questionContent.put("1C", mBinding.txt1C);
        questionContent.put("1D", mBinding.txt1D);

        questionContent.put("2A", mBinding.txt2A);
        questionContent.put("2B", mBinding.txt2B);
        questionContent.put("2C", mBinding.txt2C);
        questionContent.put("2D", mBinding.txt2D);

        questionContent.put("3A", mBinding.txt3A);
        questionContent.put("3B", mBinding.txt3B);
        questionContent.put("3C", mBinding.txt3C);
        questionContent.put("3D", mBinding.txt3D);
        showQuestion(questions);
    }

    @Override
    public void onConfirm() {
        mVM.postSelectedQuestion(answer);
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
        mBinding.txt1A.setOnClickListener(this);
        mBinding.txt1B.setOnClickListener(this);
        mBinding.txt1C.setOnClickListener(this);
        mBinding.txt1D.setOnClickListener(this);

        mBinding.txt2A.setOnClickListener(this);
        mBinding.txt2B.setOnClickListener(this);
        mBinding.txt2C.setOnClickListener(this);
        mBinding.txt2D.setOnClickListener(this);

        mBinding.txt3A.setOnClickListener(this);
        mBinding.txt3B.setOnClickListener(this);
        mBinding.txt3C.setOnClickListener(this);
        mBinding.txt3D.setOnClickListener(this);
    }

    private void showQuestion(List<Question> questions) {
        if (context.getMediaPlayer() != null && context.getMediaPlayer().isPlaying()) {
            context.getMediaPlayer().stop();
        }

        context.setQuestionTitle(this.numQuestion + "-" + (this.numQuestion + this.questions.size() - 1));

        // set tag for the purpose of onClick
        mBinding.txt1A.setTag(this.numQuestion);
        mBinding.txt1B.setTag(this.numQuestion);
        mBinding.txt1C.setTag(this.numQuestion);
        mBinding.txt1D.setTag(this.numQuestion);

        mBinding.txt2A.setTag(this.numQuestion + 1);
        mBinding.txt2B.setTag(this.numQuestion + 1);
        mBinding.txt2C.setTag(this.numQuestion + 1);
        mBinding.txt2D.setTag(this.numQuestion + 1);

        mBinding.txt3A.setTag(this.numQuestion + 2);
        mBinding.txt3B.setTag(this.numQuestion + 2);
        mBinding.txt3C.setTag(this.numQuestion + 2);
        mBinding.txt3D.setTag(this.numQuestion + 2);

        if (part == 2){
            mBinding.question1.setText(context.getString(R.string.number_question, this.numQuestion));
            mBinding.question2.setText(context.getString(R.string.number_question, this.numQuestion + 1));
            mBinding.question3.setText(context.getString(R.string.number_question, this.numQuestion + 2));
        }else if (part == 3 || part == 4){
            mBinding.txt1D.setVisibility(View.VISIBLE);
            mBinding.txt2D.setVisibility(View.VISIBLE);
            mBinding.txt3D.setVisibility(View.VISIBLE);

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
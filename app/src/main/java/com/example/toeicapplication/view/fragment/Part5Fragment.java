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
import com.example.toeicapplication.databinding.FragmentPart5Binding;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.viewmodels.ExamViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part5Fragment extends BaseFragment<ExamViewModel, FragmentPart5Binding>
        implements ExamActivity.OnConfirmAnswer, View.OnClickListener {
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
    public FragmentPart5Binding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentPart5Binding.inflate(inflater, container, attachToParent);
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
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
        layouts = new ArrayList<>();
        answer = new HashMap<>();
        questionContent = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            questionTitle.addAll(List.of(mBinding.question1, mBinding.question2, mBinding.question3, mBinding.question4));
            layouts.addAll(List.of(mBinding.layout1, mBinding.layout2, mBinding.layout3, mBinding.layout4));
        }else{
            questionTitle.add(mBinding.question1);
            questionTitle.add(mBinding.question2);
            questionTitle.add(mBinding.question3);
            questionTitle.add(mBinding.question4);

            layouts.add(mBinding.layout1);
            layouts.add(mBinding.layout2);
            layouts.add(mBinding.layout3);
            layouts.add(mBinding.layout4);
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

        questionContent.put("4A", mBinding.txt4A);
        questionContent.put("4B", mBinding.txt4B);
        questionContent.put("4C", mBinding.txt4C);
        questionContent.put("4D", mBinding.txt4D);
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

        mBinding.txt4A.setOnClickListener(this);
        mBinding.txt4B.setOnClickListener(this);
        mBinding.txt4C.setOnClickListener(this);
        mBinding.txt4D.setOnClickListener(this);

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

        mBinding.txt4A.setTag(this.numQuestion + 3);
        mBinding.txt4B.setTag(this.numQuestion + 3);
        mBinding.txt4C.setTag(this.numQuestion + 3);
        mBinding.txt4D.setTag(this.numQuestion + 3);
    }
}
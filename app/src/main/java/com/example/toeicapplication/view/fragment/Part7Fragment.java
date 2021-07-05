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
import androidx.fragment.app.FragmentActivity;

import com.example.toeicapplication.ExamActivity;
import com.example.toeicapplication.databinding.FragmentPart7Binding;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.viewmodels.ExamViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part7Fragment extends BasePartFragment<ExamViewModel, FragmentPart7Binding>
        implements ExamActivity.OnConfirmAnswer, View.OnClickListener {
    private ExamActivity examActivity;

    private static final String NUM_QUESTION = "numQuestion";
    private static final String QUESTION = "question";
    private static final String PROGRESS_ANSWER = "progressAnswer";

    private List<TextView> questionTitle;
    private Map<Integer, String> answer;
    private Map<String, TextView> questionContent;
    private final List<Question> questions = new ArrayList<>();
    private final Map<Integer, String> progressAnswer = new HashMap<>();

    private int numQuestion;

    public Part7Fragment() {
        // Required empty public constructor
    }

    public static Part7Fragment newInstance(int numQuestion,
                                            ArrayList<Question> question,
                                            @Nullable HashMap<Integer, String> progressAnswer) {
        Part7Fragment fragment = new Part7Fragment();
        Bundle args = new Bundle();
        args.putInt(NUM_QUESTION, numQuestion);
        args.putParcelableArrayList(QUESTION, question);
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

            HashMap<Integer, String> hashMap = (HashMap<Integer, String>) getArguments().getSerializable(PROGRESS_ANSWER);
            if (hashMap != null){
                progressAnswer.putAll(hashMap);
            }
        }
    }

    @Override
    public FragmentPart7Binding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentPart7Binding.inflate(inflater, container, attachToParent);
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
        examActivity = (ExamActivity) getActivity();

        if (examActivity != null) {
            examActivity.setOnConfirmAnswer(this);
            examActivity.changeButtonState(false);
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
            questionTitle.addAll(List.of(mBinding.question1, mBinding.question2, mBinding.question3, mBinding.question4, mBinding.question5));
        }else{
            questionTitle.add(mBinding.question1);
            questionTitle.add(mBinding.question2);
            questionTitle.add(mBinding.question3);
            questionTitle.add(mBinding.question4);
            questionTitle.add(mBinding.question5);
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

        questionContent.put("5A", mBinding.txt5A);
        questionContent.put("5B", mBinding.txt5B);
        questionContent.put("5C", mBinding.txt5C);
        questionContent.put("5D", mBinding.txt5D);
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
            examActivity.changeButtonState(true);
    }

    private void showQuestion(List<Question> questions) {
        Question firstQuestion = questions.get(0);
        if (firstQuestion != null){
            try {
                if (firstQuestion.getDescription() != null) {
                    InputStream is = examActivity.getAssets().open(firstQuestion.getDescription());
                    Drawable drawable = Drawable.createFromStream(is, null);
                    mBinding.imageQuestion.setImageDrawable(drawable);
                    mBinding.imageQuestion.setVisibility(View.VISIBLE);
                    mBinding.description.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                mBinding.description.setText(firstQuestion.getDescription());
                mBinding.description.setVisibility(View.VISIBLE);
                mBinding.imageQuestion.setVisibility(View.GONE);
            }
        }
        examActivity.setQuestionTitle(this.numQuestion + "-" + (this.numQuestion + this.questions.size() - 1));

        for (int i = 0; i < questions.size(); i++){
            if (i == 4) mBinding.layout5.setVisibility(View.VISIBLE);

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

        // show the answer are choice before
        questionContent.entrySet().forEach(item -> {
            TextView txt = item.getValue();
            Integer tag = (Integer) txt.getTag();

            if (txt.getText().toString().equals(progressAnswer.get(tag))) {
                txt.setBackground(answerSelectedState);
                answer.put(tag, txt.getText().toString());
            }
        });
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

        mBinding.txt5A.setOnClickListener(this);
        mBinding.txt5B.setOnClickListener(this);
        mBinding.txt5C.setOnClickListener(this);
        mBinding.txt5D.setOnClickListener(this);

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

        mBinding.txt5A.setTag(this.numQuestion + 4);
        mBinding.txt5B.setTag(this.numQuestion + 4);
        mBinding.txt5C.setTag(this.numQuestion + 4);
        mBinding.txt5D.setTag(this.numQuestion + 4);
    }
}
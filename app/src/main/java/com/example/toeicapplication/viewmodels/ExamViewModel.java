package com.example.toeicapplication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.Progress;
import com.example.toeicapplication.model.Question;
import com.example.toeicapplication.repository.ExamRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

import static com.example.toeicapplication.utilities.Utils.listeningScore;
import static com.example.toeicapplication.utilities.Utils.readingScore;

@HiltViewModel
public class ExamViewModel extends ViewModel {
    private final MutableLiveData<List<Question>> questions;
    private final MutableLiveData<Progress> progress;
    private final MutableLiveData<Map<Integer, String>> selectedQuestion;

    private final ExamRepository repository;

    @Inject
    public ExamViewModel(ExamRepository repository){
        questions = new MutableLiveData<>();
        selectedQuestion = new MutableLiveData<>();
        progress = new MutableLiveData<>();

        this.repository = repository;
        selectedQuestion.setValue(new HashMap<>());
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }

    public MutableLiveData<Progress> getProgress() {
        return progress;
    }

    public MutableLiveData<Map<Integer, String>> getSelectedQuestion() {
        return selectedQuestion;
    }

    public void postSelectedQuestion(int numQuestion, String answer){
        Map<Integer, String> selected = this.getSelectedQuestion().getValue();

        if (selected == null) selected  = new HashMap<>();

        selected.put(numQuestion, answer);
        this.getSelectedQuestion().postValue(selected);
    }

    public void postSelectedQuestion(Map<Integer, String> answer){
        Map<Integer, String> selected = this.getSelectedQuestion().getValue();

        if (selected == null) selected  = new HashMap<>();

        selected.putAll(answer);

        this.getSelectedQuestion().postValue(selected);
    }

    public void getListQuestionByCourseID(Long courseID){
        repository.getListQuestionByCourseID(this.questions, courseID);
    }

    public void getProgressByCourseID(Long courseID){
        repository.getProgressByCourseID(this.progress, courseID);
    }

    public void add(Progress progress){
        repository.add(progress);
    }

    public Map<String, Integer> calculateAnswer(){
        Map<String, Integer> result = new HashMap<>();

        List<Question> questionList = this.questions.getValue();
        Map<Integer, String> answer = this.selectedQuestion.getValue();

        if (questionList != null && answer != null) {
            int correctReading = 0;
            int correctListening = 0;

            for (int i = 0; i < questionList.size(); i++) {
                Question q = questionList.get(i);

                // reading phase
                if (i < 100){
                    if (answer.containsKey(i) && q.getAnswer().equals(answer.get(i+1))) {
                        correctReading++;
                    }
                }else {
                    if (answer.containsKey(i) && q.getAnswer().equals(answer.get(i+1))) {
                        correctListening++;
                    }
                }
            }

            int readingScore = readingScore().get(correctReading);
            int listeningScore = listeningScore().get(correctListening);
            float percent = (Float.valueOf(answer.size()) / Float.valueOf(questionList.size())) * 100;

            result.put("reading", readingScore);
            result.put("listening", listeningScore);
            result.put("totalScore", readingScore + listeningScore);
            result.put("wrong", answer.size() - (correctReading + correctListening));
            result.put("correct", correctReading + correctListening);
            result.put("completion", (int) percent);
            result.put("totalQuestion", questionList.size());
            return result;
        }
        return null;
    }
}

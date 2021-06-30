package com.example.toeicapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toeicapplication.model.entity.Progress;
import com.example.toeicapplication.model.entity.Question;
import com.example.toeicapplication.repository.ExamRepository;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.toeicapplication.utilities.Utils.listeningScore;
import static com.example.toeicapplication.utilities.Utils.readingScore;

@HiltViewModel
public class ExamViewModel extends ViewModel implements LifecycleObserver {
    private final MutableLiveData<List<Question>> questions;
    private final MutableLiveData<Map<Integer, String>> selectedQuestion;
    private final MutableLiveData<Progress> progress;

    private final ExamRepository repository;
    private final CompositeDisposable cd;

    @Inject // don't inject the compositeDisposable with the SingletonScope
    public ExamViewModel(ExamRepository repository) {
        Log.e("TAG", "Initialize ExamViewModel");
        questions = new MutableLiveData<>();
        selectedQuestion = new MutableLiveData<>();
        progress = new MutableLiveData<>();
        this.cd = new CompositeDisposable();
        this.repository = repository;

        selectedQuestion.setValue(new HashMap<>());
    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }

    public LiveData<Progress> getProgress() {
        return progress;
    }

    public LiveData<Map<Integer, String>> getSelectedQuestion() {
        return selectedQuestion;
    }

    private void setProgress(Progress progress) {
        this.progress.setValue(progress);
    }

    private void setQuestionList(List<Question> questionList) {
        this.questions.setValue(questionList);
    }

    public void postSelectedQuestion(int numQuestion, String answer) {
        Map<Integer, String> selected = this.getSelectedQuestion().getValue();

        if (selected == null) selected = new HashMap<>();

        selected.put(numQuestion, answer);
        this.selectedQuestion.postValue(selected);
    }

    public void postSelectedQuestion(Map<Integer, String> answer) {
        Map<Integer, String> selected = this.getSelectedQuestion().getValue();

        if (selected == null) selected = new HashMap<>();

        selected.putAll(answer);

        this.selectedQuestion.postValue(selected);
    }

    // this snippet is used for cache and remote call
    public void getListQuestionByCourseID(Long courseID) {
        // this observable will only run on time in the lifecycle of compositeDisposable
        cd.add(repository.getListQuestionByCourseID(courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Question>>() {
                    @Override
                    public void onSuccess(@NotNull List<Question> questionList) {
                        setQuestionList(questionList);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        setQuestionList(null);
                    }
                }));
    }

    public void getProgressByCourseID(Long courseID) {
        repository.getProgressByCourseID(courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Progress>() {
                    @Override
                    public void onSuccess(@NotNull Progress progress) {
                        Log.e("TAG", progress.toString());
                        setProgress(progress);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        setProgress(null);
                        Log.e("TAG", "Can't get the data from Progress table: " + e.getMessage());
                    }
                });
    }

    public void add(Progress progress) {
        cd.add(repository.add(progress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.e("TAG", "Insert the progress successfully"), throwable -> {
                    Log.e("TAG", "Can't get the data from Progress table: " + throwable.getMessage());
                })
        );
    }

    public void delete(Long id) {
        cd.add(repository.delete(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.e("TAG", "Delete the progress successfully");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e("TAG", "The id is not exists in the scope: " + e.getMessage());
                    }
                })
        );
    }

    public Map<String, Integer> calculateAnswer() {
        Map<String, Integer> result = new HashMap<>();

        List<Question> questionList = this.questions.getValue();
        Map<Integer, String> answer = this.selectedQuestion.getValue();

        if (questionList != null && answer != null) {
            int correctReading = 0;
            int correctListening = 0;

            for (int i = 0; i < questionList.size(); i++) {
                Question q = questionList.get(i);

                // reading phase
                if (i < 100) {
                    if (answer.containsKey(i) && q.getAnswer().equals(answer.get(i + 1))) {
                        correctReading++;
                    }
                } else {
                    if (answer.containsKey(i) && q.getAnswer().equals(answer.get(i + 1))) {
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

    @Override
    protected void onCleared() {
        Log.e("TAG", "ExamViewModel onCleared");
        super.onCleared();
        if (cd != null) {
            cd.dispose();
        }
    }
}

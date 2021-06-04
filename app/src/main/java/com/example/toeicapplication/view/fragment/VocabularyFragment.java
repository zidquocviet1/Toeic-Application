package com.example.toeicapplication.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toeicapplication.HomeActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.adapters.WordAdapter;
import com.example.toeicapplication.databinding.FragmentCourseBinding;
import com.example.toeicapplication.databinding.FragmentVocabularyBinding;
import com.example.toeicapplication.model.Course;
import com.example.toeicapplication.model.Word;
import com.example.toeicapplication.utilities.DataState;
import com.example.toeicapplication.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocabularyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class VocabularyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentVocabularyBinding binding;
    private List<Word> words;
    private WordAdapter wordAdapter;
    private HomeActivity context;
    private HomeViewModel homeVM;

    private static final long DELAY = 200;

    public VocabularyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VocabularyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VocabularyFragment newInstance(String param1, String param2) {
        VocabularyFragment fragment = new VocabularyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVocabularyBinding.inflate(inflater, container, false);
        context = (HomeActivity) getActivity();
        homeVM = new ViewModelProvider(context).get(HomeViewModel.class);

        setupRecyclerView();

        if (homeVM.getWords().getValue() == null
                || homeVM.getWords().getValue().getData() == null
                || homeVM.getWords().getValue().getData().isEmpty()){
            setupObserve();
        }else{
            setData();
        }

        binding.input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (wordAdapter != null) {
                    wordAdapter.getFilter().filter(s);
                    Log.e("TAG", s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }

    private void setupObserve(){
        homeVM.getWords().observe(getViewLifecycleOwner(), wordsSate -> {
            if (wordsSate != null){
                if (wordsSate.getStatus() == DataState.Status.LOADING){
                    context.displayLoading(true, DELAY);
                }

                if (wordsSate.getStatus() == DataState.Status.SUCCESS){
                    context.displayLoading(false, DELAY);
                    new Handler(Looper.getMainLooper())
                            .postDelayed(() -> wordAdapter.setData(wordsSate.getData()), DELAY);
                }

                if (wordsSate.getStatus() == DataState.Status.ERROR){
                    context.displayLoading(false, DELAY);
                    Log.e("Vocabulary Fragment", wordsSate.getMessage());
                }
            }
        });
    }

    private void setData(){
        wordAdapter.setData(homeVM.getWords().getValue().getData());
    }

    private void setupRecyclerView(){
        words = new ArrayList<>();

        wordAdapter = new WordAdapter(context, words);

        binding.rvVocab.setAdapter(wordAdapter);
        binding.rvVocab.setLayoutManager(new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false));
        binding.rvVocab.setHasFixedSize(true);
    }
}
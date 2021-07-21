package com.example.toeicapplication.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.HomeActivity;
import com.example.toeicapplication.adapters.WordAdapter;
import com.example.toeicapplication.databinding.FragmentVocabularyBinding;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.utilities.Status;
import com.example.toeicapplication.viewmodels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VocabularyFragment extends BaseFragment<HomeViewModel, FragmentVocabularyBinding> implements TextWatcher {
    private WordAdapter wordAdapter;
    private HomeActivity context;

    private List<Word> words;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final long DELAY = 500;

    private String mParam1;
    private String mParam2;

    public VocabularyFragment() {
        // Required empty public constructor
    }

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
    public FragmentVocabularyBinding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentVocabularyBinding.inflate(inflater, container, attachToParent);
    }

    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return requireActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();

        if (mVM.getWords().getValue() == null
                || mVM.getWords().getValue().getData() == null
                || mVM.getWords().getValue().getData().isEmpty()){
            setupObserve();
        }else{
            setData();
        }

        // chua xong phan nay
        mBinding.input.addTextChangedListener(this);
    }

    private void setupObserve(){
        mVM.getWords().observe(getViewLifecycleOwner(), wordsSate -> {
            if (wordsSate != null){
                if (wordsSate.getStatus() == Status.LOADING){
                    context.displayLoading(true, DELAY);
                }

                if (wordsSate.getStatus() == Status.SUCCESS){
                    context.displayLoading(false, DELAY);
                    new Handler(Looper.getMainLooper())
                            .postDelayed(() -> wordAdapter.setData(wordsSate.getData()), DELAY);
                }

                if (wordsSate.getStatus() == Status.ERROR){
                    context.displayLoading(false, DELAY);
                    Log.e("Vocabulary Fragment", wordsSate.getMessage());
                }
            }
        });
    }

    private void setData(){
        if (mVM.getWords().getValue() != null) wordAdapter.setData(mVM.getWords().getValue().getData());
    }

    private void setupRecyclerView(){
        words = new ArrayList<>();

        wordAdapter = new WordAdapter(context, words);

        mBinding.rvVocab.setAdapter(wordAdapter);
        mBinding.rvVocab.setLayoutManager(new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false));
        mBinding.rvVocab.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mBinding.rvVocab.setHasFixedSize(true);
    }

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
}
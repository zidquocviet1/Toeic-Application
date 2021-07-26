package com.example.toeicapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.CourseDetailActivity;
import com.example.toeicapplication.adapters.CommentAdapter;
import com.example.toeicapplication.databinding.TabReviewFragmentBinding;
import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.viewmodels.CourseDetailViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {
    private final List<Comment> comments = new ArrayList<>();
    private TabReviewFragmentBinding binding;
    private CourseDetailActivity context;
    private CommentAdapter commentAdapter;
    private CourseDetailViewModel mVM;

    // require no arguments constructor
    public ReviewFragment(){}

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = TabReviewFragmentBinding.inflate(inflater, container, false);
        context = (CourseDetailActivity) getActivity();

        setupRecyclerView();

        mVM = new ViewModelProvider(requireActivity()).get(CourseDetailViewModel.class);
        mVM.getCommentLiveData().observe(getViewLifecycleOwner(), comments -> {
            if (comments.isEmpty()){
                binding.txtNoComment.setVisibility(View.VISIBLE);
                binding.rvReview.setVisibility(View.GONE);
            }else{
                binding.txtNoComment.setVisibility(View.GONE);
                binding.rvReview.setVisibility(View.VISIBLE);
            }
            commentAdapter.setData(comments);
        });
        return binding.getRoot();
    }

    private void setupRecyclerView(){
        commentAdapter = new CommentAdapter(context, comments);

        binding.rvReview.setAdapter(commentAdapter);
        binding.rvReview.setHasFixedSize(true);
        binding.rvReview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }
}

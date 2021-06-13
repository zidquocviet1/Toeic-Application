package com.example.toeicapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.CourseDetailActivity;
import com.example.toeicapplication.adapters.CommentAdapter;
import com.example.toeicapplication.databinding.TabReviewFragmentBinding;
import com.example.toeicapplication.model.Comment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    private ArrayList<Comment> comments = new ArrayList<>();
    private TabReviewFragmentBinding binding;
    private CommentAdapter commentAdapter;
    private CourseDetailActivity context;

    // require no arguments constructor
    public ReviewFragment(){}

    public static ReviewFragment newInstance(ArrayList<Comment> comments){
        ReviewFragment reviewFragment = new ReviewFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("list_comment", comments);

        reviewFragment.setArguments(args);
        return reviewFragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.getParcelableArrayList("list_comment") != null){
            comments.addAll(getArguments().getParcelableArrayList("list_comment"));
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = TabReviewFragmentBinding.inflate(inflater, container, false);
        context = (CourseDetailActivity) getActivity();

        setupRecyclerView();
        if (comments.isEmpty()){
            binding.txtNoComment.setVisibility(View.VISIBLE);
            binding.rvReview.setVisibility(View.GONE);
        }else{
            binding.txtNoComment.setVisibility(View.GONE);
            binding.rvReview.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    private void setupRecyclerView(){
        commentAdapter = new CommentAdapter(context, comments);

        binding.rvReview.setAdapter(commentAdapter);
        binding.rvReview.setHasFixedSize(true);
        binding.rvReview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }
}

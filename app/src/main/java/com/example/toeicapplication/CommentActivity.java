package com.example.toeicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.adapters.CommentAdapter;
import com.example.toeicapplication.databinding.ActivityCommentBinding;
import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.utilities.Status;
import com.example.toeicapplication.view.fragment.CourseFragment;
import com.example.toeicapplication.viewmodels.CommentViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentActivity extends BaseActivity<CommentViewModel, ActivityCommentBinding> implements View.OnClickListener {
    private List<Comment> comments;
    private User user;
    private Course course;
    private CommentAdapter adapter;
    private int addedPosition = -1;

    @NonNull
    @NotNull
    @Override
    public Class<CommentViewModel> getViewModel() {
        return CommentViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_comment;
    }

    @Override
    public ActivityCommentBinding bindingInflater() {
        return ActivityCommentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        comments = new ArrayList<>(intent.getParcelableArrayListExtra(CourseFragment.ENTRY));
        user = intent.getParcelableExtra(CourseFragment.USER_KEY);
        course = intent.getParcelableExtra(CourseFragment.COURSE_KEY);

        loadUser();
        setupRecyclerView();

        mBinding.imgArrow.setOnClickListener(this);
        mBinding.txtPost.setOnClickListener(this);
        Objects.requireNonNull(mBinding.layoutInput.getEditText()).addTextChangedListener(initWatcher());
    }

    @Override
    public void setupObserver() {
        mVM.getCommentLiveData().observe(this, state -> {
            if (state.getStatus() == Status.SUCCESS) {
                comments.set(addedPosition, state.getData());
                new Handler(getMainLooper()).postDelayed(() -> adapter.notifyItemChanged(addedPosition), 2000);
            } else if (state.getStatus() == Status.LOADING) {
                adapter.notifyItemChanged(addedPosition, getString(R.string.comment_loading_state));
            } else {
                new Handler(getMainLooper()).postDelayed(() ->
                        adapter.notifyItemChanged(addedPosition, getString(R.string.comment_error_state)), 1500);
            }
        });
    }

    private void loadUser() {
        if (user != null) {
            mBinding.layoutComment.setVisibility(View.VISIBLE);

            CircularProgressDrawable cp = new CircularProgressDrawable(this);
            cp.setStrokeWidth(5f);
            cp.setCenterRadius(30f);
            cp.start();

            Glide.with(this)
                    .load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId())
                    .error(ContextCompat.getDrawable(this, R.drawable.ic_gray_account))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(cp)
                    .signature(new ObjectKey(AppConstants.API_ENDPOINT + "user/avatar?userId=" + user.getId()))
                    .into(mBinding.imgAvatar);
        } else {
            mBinding.layoutComment.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        adapter = new CommentAdapter(this, comments);
        adapter.setOnDataSetChangeListener(this::changeUILayout);
        adapter.setOnRetryListener(this::postComment);
        mBinding.rvComment.setAdapter(adapter);
        mBinding.rvComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        changeUILayout();
    }

    private void changeUILayout() {
        if (adapter.getItemCount() > 0) {
            mBinding.txtError.setVisibility(View.GONE);
            mBinding.rvComment.setVisibility(View.VISIBLE);
        } else {
            mBinding.txtError.setVisibility(View.VISIBLE);
            mBinding.rvComment.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBinding.imgArrow.getId()) {
            finish();
        } else if (id == mBinding.txtPost.getId()) {
            String content = Objects.requireNonNull(mBinding.layoutInput.getEditText()).getText().toString();
            Comment comment = new Comment(null, content, null, course.getId(), user.getId());

            postComment(comment);

            mBinding.layoutInput.getEditText().setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBinding.layoutInput.getWindowToken(), 0);
        }
    }

    private void postComment(@NonNull Comment comment){
        adapter.addComment(comment);
        mVM.postComment(comment);
        addedPosition = comments.indexOf(comment);
        mBinding.rvComment.smoothScrollToPosition(addedPosition);
    }

    private TextWatcher initWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    mBinding.txtPost.setEnabled(false);
                    mBinding.txtPost.setTextColor(getColor(R.color.grayish));
                } else {
                    mBinding.txtPost.setEnabled(true);
                    mBinding.txtPost.setTextColor(getColor(R.color.see_all));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
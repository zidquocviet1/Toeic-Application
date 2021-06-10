package com.example.toeicapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toeicapplication.CourseDetailActivity;
import com.example.toeicapplication.ExamActivity;
import com.example.toeicapplication.HomeActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.adapters.WordAdapter;
import com.example.toeicapplication.databinding.FragmentHomeBinding;
import com.example.toeicapplication.model.Course;
import com.example.toeicapplication.model.Word;
import com.example.toeicapplication.listeners.ItemClickListener;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment
        implements ItemClickListener, View.OnClickListener {
    private FragmentHomeBinding binding;
    private HomeViewModel homeVM;
    private HomeActivity context;

    private CourseAdapter courseAdapter;
    private WordAdapter wordsAdapter;

    private List<Course> courses;
    private List<Word> words;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        context = (HomeActivity) getActivity();
        binding.btnViewAll.setOnClickListener(this);

        observeViewModel();
        initRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Object object, int position) {
        if (object instanceof CourseAdapter) {
            Course course = courseAdapter.getItem(position);

            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("course", course);

            startActivity(intent);
        } else if (object instanceof WordAdapter) {
            // open WordInfoActivity
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnViewAll){
            context.openFragment(VocabularyFragment.class, getString(R.string.vocabulary), R.id.mnVocab);
        }
    }

    private void observeViewModel() {
        homeVM = new ViewModelProvider(context).get(HomeViewModel.class);

        homeVM.getCacheUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null)
                binding.txtHello.setText(getString(R.string.hello, user.getDisplayName()));
            else
                binding.txtHello.setText(getString(R.string.hello, "Guys"));
        });

        homeVM.getTop30Words().observe(getViewLifecycleOwner(), words -> {
            if (words != null){
                wordsAdapter.setData(words);
            }
        });

        homeVM.getCourses().observe(getViewLifecycleOwner(), courses -> {
            if (courses != null){
                courseAdapter.setData(courses);
            }
        });
    }

    private void initRecyclerView() {
        courses = new ArrayList<>();
        words = new ArrayList<>();

        courseAdapter = new CourseAdapter(context, courses, R.layout.info_course, CourseAdapter.Owner.HOME_FRAGMENT);
        wordsAdapter = new WordAdapter(context, words);

        courseAdapter.setOnItemClickListener(this);
        wordsAdapter.setOnItemClickListener(this);

        binding.rvCourse.setAdapter(courseAdapter);
        binding.rvCourse.setHasFixedSize(false);
        binding.rvCourse.setLayoutManager(new GridLayoutManager(context, 1,
                RecyclerView.HORIZONTAL, false));

        binding.rvVocab.setAdapter(wordsAdapter);
        binding.rvVocab.setHasFixedSize(false);
        binding.rvVocab.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        binding.rvVocab.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }
}
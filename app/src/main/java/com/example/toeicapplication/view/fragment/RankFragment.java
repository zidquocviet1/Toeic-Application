package com.example.toeicapplication.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.toeicapplication.R;
import com.example.toeicapplication.adapters.RankAdapter;
import com.example.toeicapplication.databinding.FragmentRankBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.viewmodels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RankFragment extends BaseFragment<HomeViewModel, FragmentRankBinding>
        implements AdapterView.OnItemClickListener {
    private ArrayAdapter<String> itemAdapter;
    private RankAdapter rankAdapter;
    private Context context;

    private List<String> items;
    private List<Course> courses;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int DEFAULT_INDEX = 0;

    private String mParam1;
    private String mParam2;

    public RankFragment() {
        // Required empty public constructor
    }

    public static RankFragment newInstance(String param1, String param2) {
        RankFragment fragment = new RankFragment();
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
    public FragmentRankBinding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentRankBinding.inflate(inflater, container, attachToParent);
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
        context = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupObserve();
    }

    private void setupRecyclerView(){
        rankAdapter = new RankAdapter(context);

        mBinding.rvRankUser.setAdapter(rankAdapter);
        mBinding.rvRankUser.setHasFixedSize(true);
    }

    private void setupObserve() {
        mVM.getCourses().observe(getViewLifecycleOwner(), courseList -> {
            if (courseList != null) {
                courses = new ArrayList<>(courseList);
                items = new ArrayList<>();

                items.add(getString(R.string.all));
                courseList.forEach(c -> items.add(c.getName()));

                itemAdapter = new ArrayAdapter<>(context, R.layout.list_item_course, items);

                mBinding.txt.setAdapter(itemAdapter);
                mBinding.txt.setDropDownBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.layout_course_detail));
                mBinding.txt.setText(itemAdapter.getItem(DEFAULT_INDEX), false);
                mBinding.txt.setOnItemClickListener(this);
            }
        });

        mVM.getListRankInfo().observe(getViewLifecycleOwner(), rankInfoList -> {
            if (rankInfoList != null) {
                rankAdapter.submitList(rankInfoList);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemName = items.get(position);
        Course course = courses
                .stream()
                .filter(c -> c.getName().equals(itemName))
                .findFirst()
                .orElse(null);

        if (course == null) {
            // get all top user in the initialize
            Log.d(AppConstants.TAG, "All of top users");
        } else {
            mVM.getRankByCourse(course);
            // get rank of user by course id
            Log.d(AppConstants.TAG, course.toString());
        }
    }
}
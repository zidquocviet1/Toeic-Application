package com.example.toeicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.adapters.CourseDetailPagerAdapter;
import com.example.toeicapplication.databinding.ActivityCourseDetailBinding;
import com.example.toeicapplication.model.CourseMapper;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.view.custom.LoadingDialog;
import com.example.toeicapplication.viewmodels.CourseDetailViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseDetailActivity extends BaseActivity<CourseDetailViewModel, ActivityCourseDetailBinding>
        implements View.OnClickListener {
    private Course course;
    private User user;
    private final List<ImageView> imgList = new ArrayList<>();
    private final List<TextView> scoreView = new ArrayList<>();
    private final List<TextView> displayNameView = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public Class<CourseDetailViewModel> getViewModel() {
        return CourseDetailViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_course_detail;
    }

    @Override
    public ActivityCourseDetailBinding bindingInflater() {
        return ActivityCourseDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        course = intent.getParcelableExtra("course");
        user = intent.getParcelableExtra("user");

        initWidget();

        if (course != null) {
            setupBaseUI();
            mVM.getCourseInfo(course.getId());
        }
        setupEvent();
    }

    @Override
    public void setupObserver() {
        if (mVM != null){
            mVM.getCourseInfoLiveData().observe(this, mapper -> {
                if (mapper != null && !mapper.isEmpty()){
                    for (int i = 0; i < 3; i++){
                        CourseMapper m = mapper.get(i);

                        CircularProgressDrawable cp = new CircularProgressDrawable(this);
                        cp.setStrokeWidth(5f);
                        cp.setCenterRadius(30f);
                        cp.start();

                        if (m != null){
                            Glide.with(this)
                                    .load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + m.getUserId())
                                    .error(ContextCompat.getDrawable(this, R.drawable.ic_gray_account))
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .placeholder(cp)
                                    .signature(new ObjectKey(AppConstants.API_ENDPOINT + "user/avatar?userId=" + m.getUserId()))
                                    .into(imgList.get(i));

                            scoreView.get(i).setText(String.valueOf(m.getScore()));
                            displayNameView.get(i).setText(m.getDisplayName());
                        }
                    }

                    mapper.stream()
                            .mapToDouble(CourseMapper::getRating)
                            .average()
                            .ifPresent(value -> mBinding.txtNumStar.setText(String.valueOf(value)));
                    mBinding.txtNumUser.setText(String.valueOf(mapper.size()));
                }else{
                    CircularProgressDrawable cp = new CircularProgressDrawable(this);
                    cp.setStrokeWidth(5f);
                    cp.setCenterRadius(30f);
                    cp.start();

                    for (int i = 0; i < 3; i++) {
                        Glide.with(this)
                                .load("")
                                .error(ContextCompat.getDrawable(this, R.drawable.ic_gray_account))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .placeholder(cp)
                                .into(imgList.get(i));
                    }
                }
            });
        }
    }

    private void initWidget(){
        imgList.add(mBinding.imgTop1);
        imgList.add(mBinding.imgTop2);
        imgList.add(mBinding.imgTop3);

        scoreView.add(mBinding.txtScore1);
        scoreView.add(mBinding.txtScore2);
        scoreView.add(mBinding.txtScore3);

        displayNameView.add(mBinding.txtDisplayName1);
        displayNameView.add(mBinding.txtDisplayName2);
        displayNameView.add(mBinding.txtDisplayName3);
    }

    private void setupBaseUI(){
        int pos = new Random().nextInt(4);

        mBinding.txtTitle.setText(course.getName());
        mBinding.imgBackground.setImageDrawable(ContextCompat.getDrawable(this, CourseAdapter.images[pos]));
        mBinding.imgBackground.setClipToOutline(true);

        TabLayout tab = mBinding.tabLayout;

        CourseDetailPagerAdapter courseDetailPagerAdapter = new CourseDetailPagerAdapter(getSupportFragmentManager(),
                getLifecycle(), 2, course);
        mBinding.viewPager.setAdapter(courseDetailPagerAdapter);
        mBinding.viewPager.setUserInputEnabled(false);
        new TabLayoutMediator(tab, mBinding.viewPager, (tab1, position) -> {
            if (position == 0){
                tab1.setText(R.string.overview);
            }else if (position == 1){
                tab1.setText(R.string.review);
            }
        }).attach();
    }

    private void setupEvent(){
        mBinding.imgTop1.setOnClickListener(this);
        mBinding.imgTop2.setOnClickListener(this);
        mBinding.imgTop3.setOnClickListener(this);
    }

    public void enrollCourse(View view) {
        if (!isFinishing()) {
            LoadingDialog.showLoadingDialog(this);

            Intent intent = new Intent(CourseDetailActivity.this, ExamActivity.class);
            intent.putExtra("course", course);
            intent.putExtra("user", user);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                LoadingDialog.dismissDialog();
                startActivity(intent);
                this.finish();
            }, 500);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        /*
        * Show User Activity when the other user click on the avatar of top 3 users
        * */
        if (id == mBinding.imgTop1.getId()){

        }else if (id == mBinding.imgTop2.getId()){

        }else if (id == mBinding.imgTop3.getId()){

        }
    }
}
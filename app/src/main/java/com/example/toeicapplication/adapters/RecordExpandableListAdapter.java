package com.example.toeicapplication.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.InfoCourseGroupBinding;
import com.example.toeicapplication.databinding.InfoUserRecordBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;

import java.util.List;
import java.util.Map;

public class RecordExpandableListAdapter extends BaseExpandableListAdapter {
    private List<Course> courses;
    private Map<Course, List<Result>> listDetail;
    private Context context;

    public RecordExpandableListAdapter(List<Course> courses, Map<Course, List<Result>> listDetail, Context context) {
        this.courses = courses;
        this.listDetail = listDetail;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return courses.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDetail.get(courses.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return courses.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDetail.get(courses.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return courses.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return listDetail.get(courses.get(groupPosition)).get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Course course = (Course) getGroup(groupPosition);
        InfoCourseGroupBinding binding;

        if (convertView == null) {
            binding = InfoCourseGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
        }else{
            binding = (InfoCourseGroupBinding) convertView.getTag();
        }

        binding.txtTitle.setText(course.toString());
        convertView.setTag(binding);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Result child = (Result) getChild(groupPosition, childPosition);
        InfoUserRecordBinding binding;

        if (convertView  == null){
            binding = InfoUserRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
        }else{
            binding = (InfoUserRecordBinding) convertView.getTag();
        }

        if (child != null){
            binding.txtScore.setText(child.getScore().toString());
        }
        convertView.setTag(binding);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

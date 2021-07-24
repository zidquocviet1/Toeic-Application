package com.example.toeicapplication.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.InfoCourseGroupBinding;
import com.example.toeicapplication.databinding.InfoUserRecordBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.utilities.Utils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class RecordExpandableListAdapter extends BaseExpandableListAdapter {
    private final List<Course> courses;
    private final Map<Course, List<Result>> listDetail;
    private final Context context;

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
        List<Result> results = listDetail.get(courses.get(groupPosition));
        return results == null || results.isEmpty() ? 0 : results.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return courses.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Result> results = listDetail.get(courses.get(groupPosition));
        return results == null || results.isEmpty() ? null : results.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return courses.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        List<Result> results = listDetail.get(courses.get(groupPosition));
        return results == null || results.isEmpty() ? childPosition : results.get(childPosition).getId();
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
        } else {
            binding = (InfoCourseGroupBinding) convertView.getTag();
        }

        binding.txtTitle.setText(course.toString());
        binding.imgGroupIndicator.setSelected(isExpanded);

        convertView.setTag(binding);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Result child = (Result) getChild(groupPosition, childPosition);
        InfoUserRecordBinding binding;

        if (convertView == null) {
            binding = InfoUserRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = (InfoUserRecordBinding) convertView.getTag();
        }

        if (child != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            binding.txtScore.setText(Html.fromHtml(context.getString(R.string.score, child.getScore()), Html.FROM_HTML_MODE_COMPACT));
            binding.txtCompleteRatio.setText(context.getString(R.string.complete_progress, child.getCompletion()));
            binding.pbProgress.setProgress(child.getCompletion());
            binding.txtReadingScore.setText(Html.fromHtml(context.getString(R.string.reading_score, child.getReadingScore()), Html.FROM_HTML_MODE_COMPACT));
            binding.txtListeningScore.setText(Html.fromHtml(context.getString(R.string.listening_score, child.getListeningScore()), Html.FROM_HTML_MODE_COMPACT));
            binding.txtCorrect.setText(Html.fromHtml(context.getString(R.string.correct_detail, child.getCorrect()), Html.FROM_HTML_MODE_COMPACT));
            binding.txtWrong.setText(Html.fromHtml(context.getString(R.string.wrong_detail, child.getWrong()), Html.FROM_HTML_MODE_COMPACT));
            binding.txtDuration.setText(Html.fromHtml(context.getString(R.string.duration_detail, Utils.convertTime(child.getDuration())), Html.FROM_HTML_MODE_COMPACT));
            binding.txtTimestamp.setText(Html.fromHtml(context.getString(R.string.timestamp_detail, child.getTimestamp().format(formatter)), Html.FROM_HTML_MODE_COMPACT));
        }else{
            binding.txtScore.setText("Have no record in this course yet.");
        }
        convertView.setTag(binding);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

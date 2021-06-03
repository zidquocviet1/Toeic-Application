package com.example.toeicapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.R;
import com.example.toeicapplication.db.model.Course;
import com.example.toeicapplication.listeners.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private final Context context;
    private List<Course> data;
    private ItemClickListener callback;

    private int[] images = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_5,
    };

    public CourseAdapter(Context context, List<Course> data){
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(ItemClickListener callback){
        this.callback = callback;
    }

    public void setData(List<Course> courses){
        this.data = new ArrayList<>(courses);
        notifyItemRangeInserted(0, data.size() -1);
    }

    @NonNull
    @NotNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.course_info, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseViewHolder holder, int position) {
        Course course = data.get(position);

        int imagesIndex = ThreadLocalRandom.current().nextInt(0, 4);
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, images[imagesIndex]));
        holder.txtName.setText(course.getName());
        holder.txtDescription.setText(course.getDescription());

        holder.itemView.setOnClickListener(l -> {
            if (callback != null){
                callback.onItemClick(this, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView txtName;
        private TextView txtDescription;

        public CourseViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.txtDescription = itemView.findViewById(R.id.txtDescription);
            this.txtName = itemView.findViewById(R.id.txtName);
        }
    }
}

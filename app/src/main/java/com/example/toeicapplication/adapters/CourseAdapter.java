package com.example.toeicapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.R;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.listeners.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private final Context context;
    private List<Course> data;
    private ItemClickListener callback;
    private final int layoutResource;
    private final Owner owner;

    public enum Owner {
        HOME_FRAGMENT,
        COURSE_FRAGMENT
    }

    public static final int[] images = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_5,
    };

    public CourseAdapter(Context context, List<Course> data, int layoutResource, Owner owner){
        this.context = context;
        this.data = data;
        this.layoutResource  = layoutResource;
        this.owner = owner;
    }

    public void setOnItemClickListener(ItemClickListener callback){
        this.callback = callback;
    }

    public void setData(List<Course> courses){
        this.data = new ArrayList<>(courses);
        notifyItemRangeInserted(0, data.size() -1);
    }

    public Course getItem(int position){
        return data.get(position);
    }

    @NonNull
    @NotNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(layoutResource, parent, false);
        return new CourseViewHolder(view, owner);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseViewHolder holder, int position) {
        Course course = data.get(position);

        int imagesIndex = ThreadLocalRandom.current().nextInt(0, 4);
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, images[imagesIndex]));
        holder.txtName.setText(course.getName());
        holder.txtDescription.setText(course.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (callback != null){
                callback.onItemClick(this, position);
            }
        });

        if (owner == Owner.COURSE_FRAGMENT){
            holder.itemView.setOnClickListener(null);
            holder.rbStar.setRating(course.getRating() == null ? 0.0f : course.getRating());
            holder.txtComment.setText(context.getString(R.string.comment,
                                        course.getComment() == null ? 0: course.getComment().size()));
            holder.txtRatio.setText(context.getString(R.string.comment_ratio, String.format(Locale.getDefault(),
                    "%.1f", course.getRating() == null ? 0.0 : course.getRating())));

            holder.txtViewAll.setOnClickListener(l -> {
                if (callback != null){
                    callback.onItemClick(this, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private RatingBar rbStar;
        private TextView txtComment, txtViewAll, txtDescription, txtName, txtRatio;

        public CourseViewHolder(@NonNull @NotNull View itemView, Owner owner) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.txtDescription = itemView.findViewById(R.id.txtDescription);
            this.txtName = itemView.findViewById(R.id.txtName);

            if (owner == Owner.COURSE_FRAGMENT){
                rbStar = itemView.findViewById(R.id.rbStar);
                txtComment = itemView.findViewById(R.id.txtComment);
                txtViewAll = itemView.findViewById(R.id.txtViewAll);
                txtRatio = itemView.findViewById(R.id.txtRatio);
            }
        }
    }
}

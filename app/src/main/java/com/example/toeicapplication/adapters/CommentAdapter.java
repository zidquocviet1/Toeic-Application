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
import com.example.toeicapplication.model.Comment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH>{
    private List<Comment> comments;
    private Context context;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @NotNull
    @Override
    public CommentAdapter.CommentVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_review, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentAdapter.CommentVH holder, int position) {
        Comment comment = comments.get(position);

        holder.imgAvatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_1));
        holder.review.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentVH extends RecyclerView.ViewHolder{
        private final ImageView imgAvatar;
        private final TextView review;

        public CommentVH(@NonNull @NotNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            review = itemView.findViewById(R.id.txtReview);
        }
    }
}

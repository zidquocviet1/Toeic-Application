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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.R;
import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.utilities.AppConstants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH>{
    private List<Comment> comments;
    private Context context;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    public void setData(List<Comment> data){
        comments.addAll(data);
        notifyItemRangeChanged(0, data.size());
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

        holder.review.setText(comment.getContent());

        CircularProgressDrawable cp = new CircularProgressDrawable(context);
        cp.setStrokeWidth(5f);
        cp.setCenterRadius(30f);
        cp.start();

        Glide.with(context)
                .load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + comment.getUserId())
                .error(ContextCompat.getDrawable(context, R.drawable.ic_gray_account))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(cp)
                .signature(new ObjectKey(AppConstants.API_ENDPOINT + "user/avatar?userId=" + comment.getUserId()))
                .into(holder.imgAvatar);
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

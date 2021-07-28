package com.example.toeicapplication.adapters;

import android.content.Context;
import android.graphics.Paint;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH> {
    private final List<Comment> comments;
    private final Context context;
    private OnDataSetChangeListener callback;
    private OnRetryListener retryListener;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public interface OnDataSetChangeListener {
        void onDataSetChange();
    }

    public interface OnRetryListener {
        void onErrorClick(Comment comment);
    }

    public void setOnDataSetChangeListener(OnDataSetChangeListener callback) {
        this.callback = callback;
    }

    public void setOnRetryListener(OnRetryListener callback) {
        this.retryListener = callback;
    }

    public void setData(List<Comment> data) {
        comments.addAll(data);
        notifyItemRangeChanged(0, data.size());
        if (callback != null) callback.onDataSetChange();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        notifyItemInserted(comments.size());
        if (callback != null) callback.onDataSetChange();
    }

    private void deleteComment(int position) {
        comments.remove(position);
        notifyItemRemoved(position);
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

        LocalDateTime timestamp = comment.getTimestamp();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

        holder.review.setText(comment.getContent());
        holder.txtState.setText(timestamp == null ? context.getString(R.string.comment_loading_state) : timestamp.format(formatter));
        holder.txtState.setPaintFlags(0);
        holder.txtState.setTextColor(context.getColor(android.R.color.darker_gray));
        holder.imgAvatar.setEnabled(true);
        holder.review.setEnabled(true);

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
    public void onBindViewHolder(@NonNull @NotNull CommentVH holder, int position, @NonNull @NotNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            String state = (String) payloads.get(0);

            if (state.equals(context.getString(R.string.comment_loading_state))) {
                holder.txtState.setText(state);
                holder.txtState.setPaintFlags(0);
                holder.txtState.setTextColor(context.getColor(android.R.color.darker_gray));
                holder.imgAvatar.setEnabled(false);
                holder.review.setEnabled(false);
            } else if (state.equals(context.getString(R.string.comment_error_state))) {
                holder.txtState.setText(state);
                holder.txtState.setPaintFlags(holder.txtState.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                holder.txtState.setTextColor(context.getColor(android.R.color.holo_red_light));
                holder.txtState.setClickable(true);
                holder.txtState.setFocusable(true);
                holder.imgAvatar.setEnabled(false);
                holder.review.setEnabled(false);
                holder.txtState.setOnClickListener(v -> {
                    if (retryListener != null) {
                        Comment c = comments.get(position);
                        deleteComment(position);
                        retryListener.onErrorClick(c);
                    }
                });
            }
        } else super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentVH extends RecyclerView.ViewHolder {
        private final ImageView imgAvatar;
        private final TextView review, txtState;

        public CommentVH(@NonNull @NotNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            review = itemView.findViewById(R.id.txtReview);
            txtState = itemView.findViewById(R.id.txtState);
        }
    }
}

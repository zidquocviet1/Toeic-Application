package com.example.toeicapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.InfoUserRankBinding;
import com.example.toeicapplication.model.RankInfo;
import com.example.toeicapplication.model.entity.RemoteUser;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.utilities.AppConstants;

import org.jetbrains.annotations.NotNull;

public class RankAdapter extends ListAdapter<RankInfo, RankAdapter.RankViewHolder> {
    private Context context;
    private OnItemClickListener callback;
    private static final int[] ORDINAL_SYMBOL = {R.string.second_symbol, R.string.third_symbol, R.string.fourth_symbol};

    public interface OnItemClickListener{
        void onItemClick(RemoteUser item);
    }

    public void setOnItemClickListener(OnItemClickListener callback){
        this.callback = callback;
    }

    public RankAdapter(Context context){
        this(new DiffUtil.ItemCallback<RankInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull @NotNull RankInfo oldItem, @NonNull @NotNull RankInfo newItem) {
                return oldItem.getUser().getId().equals(newItem.getUser().getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull @NotNull RankInfo oldItem, @NonNull @NotNull RankInfo newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
    }
    protected RankAdapter(@NonNull @NotNull DiffUtil.ItemCallback<RankInfo> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @NotNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_user_rank, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RankViewHolder holder, int position) {
        RankInfo item = getItem(position);
        int index = getCurrentList().indexOf(item);
        String ordinal = index > 1 ? context.getString(ORDINAL_SYMBOL[2], index + 2)
                : (index == 0 ? context.getString(ORDINAL_SYMBOL[0], index + 2)
                : context.getString(ORDINAL_SYMBOL[1], index + 2));

        holder.bind(item, ordinal, context);
        holder.itemView.setOnClickListener(v -> {
            if (callback != null) callback.onItemClick(item.getUser());
        });
    }

    public static class RankViewHolder extends RecyclerView.ViewHolder {
        InfoUserRankBinding binding;

        public RankViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = InfoUserRankBinding.bind(itemView);
        }

        public void bind(RankInfo item, String ordinal, Context context){
            binding.txtDisplayName.setText(item.getUser().getDisplayName());
            binding.txtScore.setText(String.valueOf(item.getResult().getScore()));
            binding.txtRank.setText(ordinal);

            CircularProgressDrawable cp = new CircularProgressDrawable(context);
            cp.setStrokeWidth(5f);
            cp.setCenterRadius(30f);
            cp.start();

            Glide.with(context)
                    .load(AppConstants.API_ENDPOINT + "user/avatar?userId=" + item.getUser().getId())
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_gray_account))
                    .placeholder(cp)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .signature(new ObjectKey(AppConstants.API_ENDPOINT + "user/avatar?userId=" + item.getUser().getId()))
                    .into(binding.imgUser);
        }
    }
}

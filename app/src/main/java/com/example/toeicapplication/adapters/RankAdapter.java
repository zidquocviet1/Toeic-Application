package com.example.toeicapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.R;
import com.example.toeicapplication.databinding.InfoUserRankBinding;
import com.example.toeicapplication.model.RankInfo;

import org.jetbrains.annotations.NotNull;

public class RankAdapter extends ListAdapter<RankInfo, RankAdapter.RankViewHolder> {
    private Context context;
    private static final int[] ORDINAL_SYMBOL = {R.string.second_symbol, R.string.third_symbol, R.string.fourth_symbol};

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

        holder.bind(item, ordinal);
    }

    public static class RankViewHolder extends RecyclerView.ViewHolder {
        InfoUserRankBinding binding;

        public RankViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = InfoUserRankBinding.bind(itemView);
        }

        public void bind(RankInfo item, String ordinal){
            binding.txtDisplayName.setText(item.getUser().getDisplayName());
            binding.txtScore.setText(String.valueOf(item.getResult().getScore()));
            binding.txtRank.setText(ordinal);
        }
    }
}

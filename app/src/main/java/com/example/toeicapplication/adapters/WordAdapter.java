package com.example.toeicapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.R;
import com.example.toeicapplication.model.Word;
import com.example.toeicapplication.listeners.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> implements Filterable {
    private List<Word> data;
    private List<Word> dataFiltered;
    private final Context context;
    private ItemClickListener callback;

    public WordAdapter(Context context, List<Word> data){
        this.context = context;
        this.data = data;
        this.dataFiltered = this.data;
    }

    public void setOnItemClickListener(ItemClickListener callback){
        this.callback = callback;
    }

    public void setData(List<Word> words){
        this.data = words;
        notifyItemRangeInserted(0, data.size() -1);
    }

    @NonNull
    @NotNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.info_word, parent, false);
        return new WordAdapter.WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WordViewHolder holder, int position) {
        Word word = data.get(position);

        holder.chkWasLearnt.setChecked(word.isWasLearnt());
        holder.txtName.setText(word.getName());
        holder.txtDes.setText(word.getDescription());
        holder.txtPro.setText(word.getPronounce());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.onItemClick(this, position);
                }
            }
        });

        holder.chkWasLearnt.setOnClickListener(v -> {
            word.setWasLearnt(!word.isWasLearnt());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // not complete yet
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataFiltered = data;
                } else {
                    List<Word> filteredList = new ArrayList<>();
                    for (Word row : data) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    dataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataFiltered = new ArrayList<>((List<Word>)filterResults.values);

                notifyItemRangeChanged(0, dataFiltered.size() - 1);
            }
        };
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder{
        private final CheckBox chkWasLearnt;
        private final TextView txtName;
        private final TextView txtDes;
        private final TextView txtPro;

        public WordViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            chkWasLearnt = itemView.findViewById(R.id.chkWasLearnt);
            txtName = itemView.findViewById(R.id.txtName);
            txtDes = itemView.findViewById(R.id.txtDes);
            txtPro = itemView.findViewById(R.id.txtPro);
        }
    }
}

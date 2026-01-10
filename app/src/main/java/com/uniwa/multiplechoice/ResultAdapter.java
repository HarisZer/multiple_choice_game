package com.uniwa.multiplechoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private final List<Result> results;

    public ResultAdapter(List<Result> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Result r = results.get(position);
        holder.tvName.setText("Όνομα: " + r.getName());
        holder.tvAM.setText("ΑΜ: " + r.getAm());
        holder.tvScore.setText("Σκορ: " + r.getScore());
        holder.tvDate.setText("Ημερομηνία: " + r.getDateTime());

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAM, tvScore, tvDate;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAM = itemView.findViewById(R.id.tvAM);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}

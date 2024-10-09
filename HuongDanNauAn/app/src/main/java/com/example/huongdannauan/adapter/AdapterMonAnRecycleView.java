package com.example.huongdannauan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.huongdannauan.R;
import com.example.huongdannauan.model.MonAn;

import java.util.List;

public class AdapterMonAnRecycleView extends RecyclerView.Adapter<AdapterMonAnRecycleView.RecipeViewHolder>{
    private Context context;
    private List<MonAn> monAns;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(MonAn monAn);
    }

    public AdapterMonAnRecycleView(Context context, List<MonAn> monAns, OnItemClickListener listener) {
        this.context = context;
        this.monAns = monAns;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        MonAn monAn = monAns.get(position);
        holder.textViewTitle.setText(monAn.getTitle());
        Glide.with(context)
                .load(monAn.getImage())
                .into(holder.imageViewRecipe);
        // Gán sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(monAn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monAns.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewRecipe;
        TextView textViewTitle;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipe = itemView.findViewById(R.id.imageViewRecipe);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }
}

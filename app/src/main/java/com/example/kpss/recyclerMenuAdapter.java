package com.example.kpss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerMenuAdapter extends RecyclerView.Adapter <RecyclerMenuAdapter.MyViewHolder> {

    private ArrayList <MenuAnaliz> dersler;
    private RecyclerViewClickListener clickListener;

    public RecyclerMenuAdapter(ArrayList<MenuAnaliz> dersler, RecyclerViewClickListener clickListener) {
        this.dersler = dersler;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDers.setText(dersler.get(position).getDersName());
        holder.tvSoruSayisi.setText(String.valueOf(dersler.get(position).getSoruSayisi()));
    }

    @Override
    public int getItemCount() {
        return dersler.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDers, tvSoruSayisi;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDers = itemView.findViewById(R.id.tvDers);
            tvSoruSayisi = itemView.findViewById(R.id.tvSoruSayisi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }
}

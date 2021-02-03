package com.example.kpss;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Post> dataArrayList;
    private Activity activity;

    public MainAdapter(Activity activity, ArrayList<Post> dataArrayList){
        this.activity=activity;
        this.dataArrayList=dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post mPost = dataArrayList.get(position);

        holder.img_post_thumbnail.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_scale_animation));

        //Picasso.get().load(dataArrayList.get(position).getPostImage()).into(holder.img_post_thumbnail);
        Picasso.get().load(mPost.getPostImage()).into(holder.img_post_thumbnail);

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_post_thumbnail;
        CardView cardView;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.action_container);
            img_post_thumbnail = itemView.findViewById(R.id.post_img_id);
            cardView = itemView.findViewById(R.id.card_view_id);
        }
    }
}

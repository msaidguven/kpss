package com.example.kpss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class ReceyclerPostAdapter extends RecyclerView.Adapter<ReceyclerPostAdapter.MyViewHolder> {
    private Context mContext;
    private List<PostModel> mData;

    public ReceyclerPostAdapter(Context mContext, List<PostModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        view = mLayoutInflater.inflate(R.layout.cardview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        holder.img_post_thumbnail.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
*/
        Picasso.get().load(mData.get(position).getPostImage()).into(holder.img_post_thumbnail);
        //AltexImageDownloader.writeToDisk(getApplicationContext(), "resimUrl", "isim");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostViewActivity.class);
                intent.putExtra("postID", mData.get(position).getPostID());
                intent.putExtra("time", mData.get(position).getTime());
                intent.putExtra("time1", mData.get(position).getTime1());
                intent.putExtra("dersID", mData.get(position).getDersID());
                intent.putExtra("konuID", mData.get(position).getKonuID());
                //intent.putExtra("konuName", mData.get(position).getKonuName());
                intent.putExtra("dersName", mData.get(position).getDersName());
                //intent.putExtra("dersName", getDersName);
                //intent.putExtra("konuName", mData.get(position).getDersName());
                mContext.startActivity(intent);
                //((Activity) mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_post_thumbnail;
        CardView cardView;
        LinearLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.action_container);
            img_post_thumbnail = itemView.findViewById(R.id.post_img_id);
            cardView = itemView.findViewById(R.id.card_view_id);
        }
    }
}

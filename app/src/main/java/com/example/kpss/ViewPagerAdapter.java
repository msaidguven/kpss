package com.example.kpss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<PostModel> mPostModel;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context mContext, List<PostModel> mPostModel){
        this.mContext = mContext;
        this.mPostModel = mPostModel;

        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return mPostModel.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.activity_post,container,false);

        ImageView imageView = view.findViewById(R.id.ImageView_post_image);
        ImageView buttonSonrakiSoru = view.findViewById(R.id.buttonSonrakiSoru);


        buttonSonrakiSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(mContext, "Son Sorudasın", Toast.LENGTH_SHORT).show();
                }
                //Intent intent = getIntent();
                //finish();
                //startActivity(intent);
        });



        PostModel postModel = mPostModel.get(position);
        Picasso.get().load(postModel.getPostImage()).into(imageView);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

package com.example.kpss;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<PostModel> mPostModel;
    private Context mContext;
    private LayoutInflater layoutInflater;

    private String vee_id;
    private String secenek;
    private String d_cevap;
    private String postID;

    TextView textView_aciklama, textView_gosterge;
    Button btnA, btnB, btnC, btnD, btnE;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    View post_setting;

    public ViewPagerAdapter(Context mContext, List<PostModel> mPostModel) {
        this.mContext = mContext;
        this.mPostModel = mPostModel;
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        //layoutInflater = LayoutInflater.from(mContext);

    }



    @Override
    public int getCount() {
        return mPostModel.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem (@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.activity_post, container, false);






        PostModel postModel = mPostModel.get(position);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        ImageView imageView = view.findViewById(R.id.ImageView_post_image);
        ImageView buttonSonrakiSoru = view.findViewById(R.id.buttonSonrakiSoru);

        post_setting = view.findViewById(R.id.post_setting);
        post_setting.setVisibility(View.VISIBLE);

        textView_aciklama = view.findViewById(R.id.textView_aciklama);

        btnA = view.findViewById(R.id.A);
        btnB = view.findViewById(R.id.B);
        btnC = view.findViewById(R.id.C);
        btnD = view.findViewById(R.id.D);
        btnE = view.findViewById(R.id.E);

        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);
        btnE.setEnabled(true);
        btnA.setBackgroundColor(Color.BLUE);
        btnB.setBackgroundColor(Color.BLUE);
        btnC.setBackgroundColor(Color.BLUE);
        btnD.setBackgroundColor(Color.BLUE);
        btnE.setBackgroundColor(Color.BLUE);
        post_setting.setVisibility(View.GONE);



        buttonSonrakiSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Son Sorudasın", Toast.LENGTH_SHORT).show();
            }
            //Intent intent = getIntent();
            //finish();
            //startActivity(intent);

        });


        Picasso.get().load(postModel.getPostImage()).into(imageView);



        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }



    public void secenekClick (View v){

        Button vee = (Button) v;
        vee_id = vee.getText().toString();

        Toast.makeText(mContext, vee_id, Toast.LENGTH_SHORT).show();

        if (vee_id.equals(d_cevap)) {
            Toast.makeText(mContext, "Tebrikler doğru cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.GREEN);
            textView_aciklama.setTextColor(Color.GREEN);
            textView_aciklama.setText("Tebrikler doğru vecap verdiniz");
            //analizCevapIncrement = "dogru_sayisi";
            //istatistikleriKaydet();
        } else if (vee_id.equals("Bilinmiyor")) {
            textView_aciklama.setTextColor(Color.BLUE);
            textView_aciklama.setText("Bu soruya doğru cevap eklenmemiş. Çözüm ekleyerek doğru cevabın bulunmasına yardım edebilirsin.");
        } else {
            Toast.makeText(mContext, "Yanlış cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.RED);
            textView_aciklama.setTextColor(Color.RED);
            textView_aciklama.setText("Yanlış cevap verdiniz. Doğru cevap " + d_cevap + " olacak.");
            //analizCevapIncrement = "yanlis_sayisi";
            //istatistikleriKaydet();
        }


        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        btnE.setEnabled(false);

        DocumentReference postCevap_increment = mFirestore.collection("Posts").document(postID);
        postCevap_increment.update(vee_id, FieldValue.increment(1));

    }








}

package com.example.kpss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostListActivity extends AppCompatActivity {
    List<PostModel> postList;
    private FirebaseFirestore mFirestore;

    String d_cevap;
    String postID;
    String btn_getText;
    String getDersName;
    String getKonuName;
    String dersID;
    String konuID;
    String userID;
    String post_image;
    String time;
    String time1;
    private int menuSoruSayisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        mFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        dersID = intent.getStringExtra("dersID");
        time = intent.getStringExtra("time");
        time1 = intent.getStringExtra("time1");
        konuID = intent.getStringExtra("konuID");
        getKonuName = intent.getStringExtra("konuName");
        getDersName = intent.getStringExtra("dersName");
        menuSoruSayisi = intent.getIntExtra("menuSoruSayisi", menuSoruSayisi);

        ViewPager viewPager = findViewById(R.id.viewPager);
        postList = new ArrayList<>();

/*
        String s = "1612265125463";
        long timestamp = Long.parseLong(s);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateStr = sdf.format( timestamp);
*/

        mFirestore.collection("Posts")
                .whereEqualTo("konuID", konuID)
                .orderBy("time1", Query.Direction.DESCENDING)
                .startAt(Long.parseLong(time1))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        postID = document.getId();
                        post_image = document.get("post_image").toString();
                        postList.add(new PostModel(postID, post_image));
                    }
                    Toast.makeText(getApplicationContext(), String.valueOf(task.getResult().size()), Toast.LENGTH_SHORT).show();
                    ViewPagerAdapter adapter = new ViewPagerAdapter(PostListActivity.this, postList);
                    viewPager.setAdapter(adapter);
                }
            }
        });

    }
}
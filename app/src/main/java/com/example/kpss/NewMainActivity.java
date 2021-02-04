package com.example.kpss;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewMainActivity extends AppCompatActivity {

    private boolean isScrolling;
    private boolean isLastItemReached;
    private GridLayoutManager gridLayoutManager;
    private List<PostModel> postList;


    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<Post> dataArrayList = new ArrayList<>();
    MainAdapter adapter;
    int startLimit = 18, limit = 10;


    private FirebaseFirestore mFirestore;
    String userName;
    String user_image;
    String postID, post_image;
    private String d_cevap;
    private String time;
    private String time1;
    private String konuID;


    private DocumentSnapshot lastVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        adapter = new MainAdapter(NewMainActivity.this, dataArrayList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        mFirestore = FirebaseFirestore.getInstance();

        recycler();

/*
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (isLastItemReached == false) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                            getData();
                    }
                }
            }
        });
*/

    }

    private void getData() {
        /*
        progressBar.setVisibility(View.VISIBLE);
        mFirestore.collection("Posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(limit)
                .startAfter(lastVisible)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        post_image = document.get("post_image").toString();
                        postID = document.getId();
                        postList.add(new Post(postID, post_image));
                    }
                    Toast.makeText(getApplicationContext(), "Next page loaded", Toast.LENGTH_SHORT).show();
                    ReceyclerPostAdapter myAdapter = new ReceyclerPostAdapter(NewMainActivity.this, postList);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(task.getResult().size()), Toast.LENGTH_SHORT).show();
                    if (task.getResult().size() < limit) {
                        isLastItemReached = true;
                        progressBar.setVisibility(View.GONE);
                    }
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }
            }
        });
         */
    }


    private void recycler() {

        postList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        mFirestore.collection("Posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(startLimit)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        post_image = document.get("post_image").toString();
                        postID = document.getId();
                        time = document.get("time").toString();
                        d_cevap = document.get("d_cevap").toString();
                        time1 = document.get("time1") == null ? "0" : document.get("time1").toString();
                        konuID = document.get("konuID").toString();
                        postList.add(new PostModel(postID, post_image, time, time1, konuID, d_cevap));
                    }

                    progressBar.setVisibility(View.GONE);

                    ReceyclerPostAdapter myAdapter = new ReceyclerPostAdapter(NewMainActivity.this, postList);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(myAdapter);

                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                    Toast.makeText(getApplicationContext(), "First page loaded", Toast.LENGTH_SHORT).show();

                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);

                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            int firstVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                            int visibleItemCount = gridLayoutManager.getChildCount();
                            int totalItemCount = gridLayoutManager.getItemCount();

                            if (isScrolling && (firstVisibleItem + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling = false;

                                mFirestore.collection("Posts")
                                        .orderBy("time", Query.Direction.DESCENDING)
                                        .limit(limit)
                                        .startAfter(lastVisible)
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                post_image = document.get("post_image").toString();
                                                postID = document.getId();
                                                time = document.get("time").toString();
                                                d_cevap = document.get("d_cevap").toString();
                                                time1 = document.get("time1") == null ? "0" : document.get("time1").toString();
                                                konuID = document.get("konuID").toString();
                                                postList.add(new PostModel(postID, post_image, time, time1, konuID, d_cevap));
                                            }

                                            myAdapter.notifyDataSetChanged();
                                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                                            if (task.getResult().size() < limit) {
                                                isLastItemReached = true;
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }

                                    }
                                });


                            }
                        }
                    };


                    recyclerView.addOnScrollListener(onScrollListener);

                }
            }
        });
    }


}
package com.example.kpss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class KonularActivity extends AppCompatActivity {

    private boolean isScrolling;
    private boolean isLastItemReached;
    private GridLayoutManager gridLayoutManager;
    private List<PostModel> postList;


    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<PostModel> dataArrayList = new ArrayList<>();
    private MainAdapter adapter;
    int startLimit = 12, limit = 6;


    private FirebaseFirestore mFirestore;
    String userName;
    String user_image;
    String postID, post_image;
    private String d_cevap;
    private String time;
    private String time1;
    private String konuID;

    private DocumentSnapshot lastVisible;

    private ArrayList<MenuAnaliz> dersler;

    private RecyclerMenuAdapter recyclerMenuAdapter;
    private RecyclerMenuAdapter.RecyclerViewClickListener clickListener;


    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    ListView listViewKonular;
    int menuSoruSayisi;
    String dersID;
    String userID;
    String getDersName;
    String getKonuName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konular);

        mToolbar = findViewById(R.id.konularToolbar);
        //listViewKonular = findViewById(R.id.listViewKonular);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        //recyclerView = findViewById(R.id.recycler_view);
        recyclerView = findViewById(R.id.recycler_view);

        adapter = new MainAdapter(KonularActivity.this, dataArrayList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progress_bar1);

        Intent intent = getIntent();
        dersID = intent.getStringExtra("dersID");
        konuID = intent.getStringExtra("konuID");
        getKonuName = intent.getStringExtra("getKonuName");
        getDersName = intent.getStringExtra("menuName");
        menuSoruSayisi = intent.getIntExtra("menuSoruSayisi", menuSoruSayisi);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getDersName);
        getSupportActionBar().setSubtitle(getKonuName);
        getSupportActionBar().setIcon(R.drawable.icon_setting);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //userID = mAuth.getCurrentUser().getUid();

        menu_view();
        recycler();
    }


    private void recycler123() {
/*
        recyclerView = findViewById(R.id.recycler_view);
        dersler = new ArrayList<>();
        setOnClickListener();
        recyclerMenuAdapter = new RecyclerMenuAdapter(dersler, clickListener);
        recyclerView.setAdapter(recyclerMenuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirestore.collection("Menuler")
                .whereEqualTo("dersID", dersID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String menuID = document.getId();
                                String menuName = document.getString("menu");
                                menuSoruSayisi = Integer.parseInt(document.get("soru_sayisi").toString());
                                dersler.add(new MenuAnaliz(menuID, menuName, menuSoruSayisi));

                            }
                            recyclerMenuAdapter.notifyDataSetChanged();
                        }
                    }
                });

 */
    }




    private void recycler() {

        postList = new ArrayList<>();

        mFirestore.collection("Posts")
                .whereEqualTo("konuID", konuID)
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

                    ReceyclerPostAdapter myAdapter = new ReceyclerPostAdapter(KonularActivity.this, postList);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(myAdapter);

                    if (task.getResult().size() > 0) {
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        progressBar.setVisibility(View.VISIBLE);
                    }
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

                            if (isScrolling && (firstVisibleItem + visibleItemCount == totalItemCount - 2) && !isLastItemReached) {
                                isScrolling = false;
                                mFirestore.collection("Posts")
                                        .whereEqualTo("konuID", konuID)
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

                                            if (task.getResult().size() > 0) {
                                                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                progressBar.setVisibility(View.VISIBLE);

                                            }

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
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }




    private void menu_view() {

        Toast.makeText(getApplicationContext(), getDersName, Toast.LENGTH_SHORT).show();


        ChipGroup chipGroup = findViewById(R.id.chip_group_main);
        mFirestore.collection("Menuler")
                .whereEqualTo("dersID", dersID)
                .orderBy("siralama")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String menuName = document.getString("menu");
                                String menuID = document.getId();
                                int menuSoruSayisi = Integer.parseInt(document.get("soru_sayisi").toString());

                                Chip chip = new Chip(KonularActivity.this);
                                chip.setText(menuName);
                                chip.setChipBackgroundColorResource(R.color.teal_200);
                                //chip.setBackgroundColor(getColorStateList(Color.BLUE));
                                chip.setCloseIconVisible(false);
                                chip.setTextColor(getResources().getColor(R.color.black));
                                chip.setTextAppearance(R.style.editText);
                                //chipGroup.addView(chip);

                                chip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Chip menu = (Chip) v;
                                        String menuName = menu.getText().toString();
                                        String dersID = (String) menu.getTag();

                                        Toast.makeText(getApplicationContext(), dersID, Toast.LENGTH_SHORT).show();

                                        Intent dersActivity = new Intent(KonularActivity.this, KonularActivity.class);
                                        dersActivity.putExtra("dersID", dersID);
                                        dersActivity.putExtra("menuName", menuName);
                                        dersActivity.putExtra("menuSoruSayisi", menuSoruSayisi);
                                        startActivity(dersActivity);
                                    }
                                });
                            }
                        }
                    }
                });
    }



}
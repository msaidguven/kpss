package com.example.kpss;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private boolean isScrolling;
    private boolean isLastItemReached;
    private GridLayoutManager gridLayoutManager;
    private List<PostModel> postList;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<PostModel> dataArrayList = new ArrayList<>();
    MainAdapter adapter;
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
    private TextView textView_userName;
    private TextView btnLogin;
    private TextView btnRegister;
    private ImageView imageView;
    private Toolbar mToolbar;

    private String menuName;
    private String menuID;
    private int menuSoruSayisi;
    Chip chip;
    private String dersName;


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Çıkış Yapmak istiyor musunuz?");
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Evet'e basılınca yapılacak işlemleri yazınız
                finish();
            }
        });
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hayır'a baslınca yapılacak işmeleri yazınız
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        postList = new ArrayList<>();

        init();
        menu_view();
        recycler();

    }

    private void init() {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        adapter = new MainAdapter(MainActivity.this, dataArrayList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        textView_userName = findViewById(R.id.textView_userName);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        imageView = findViewById(R.id.imageView);
        mToolbar = findViewById(R.id.mainToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sorularla KPSS");
        getSupportActionBar().setIcon(R.drawable.icon_menu);

        if (mAuth.getCurrentUser() == null) {
            textView_userName.setVisibility(View.GONE);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    onRestart();
                    startActivity(loginIntent);
                }
            });
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            });
        } else {
            String user_id = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        userName = task.getResult().getString("name");
                        user_image = task.getResult().getString("image");
                        textView_userName.setText(userName);
                        Picasso.get().load(user_image).into(imageView);
                    }
                }
            });

            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_signOut) {
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "Çıkış yaptınız", Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());
        }

        if (item.getItemId() == R.id.menu_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (item.getItemId() == R.id.menu_addPost) {
            Intent settingsIntent = new Intent(MainActivity.this, AddPost.class);
            startActivity(settingsIntent);
            finish();
        }

        if (item.getItemId() == R.id.menu_recycler) {
            Intent recyclerActivity = new Intent(MainActivity.this, RecyclerActivity.class);
            startActivity(recyclerActivity);
        }

        if (item.getItemId() == R.id.menu_addCategory) {
            Intent settingsIntent = new Intent(MainActivity.this, AddCategoryActivity.class);
            startActivity(settingsIntent);
        }

        if (item.getItemId() == R.id.menu_new_main_recycler) {
            Intent settingsIntent = new Intent(MainActivity.this, NewMainActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

    private void recycler() {
        postList = new ArrayList<>();
        ReceyclerPostAdapter myAdapter = new ReceyclerPostAdapter(MainActivity.this, postList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myAdapter);

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
                    ReceyclerPostAdapter myAdapter = new ReceyclerPostAdapter(MainActivity.this, postList);
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


    private void recyclerDers(String menuID) {

        postList = new ArrayList<>();
        ReceyclerPostAdapter myAdapter = new ReceyclerPostAdapter(MainActivity.this, postList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myAdapter);

        mFirestore.collection("Posts")
                .whereEqualTo("dersID", menuID)
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
                        postList.add(new PostModel(postID, post_image, time, time1, konuID, d_cevap, dersName));
                    }

                    progressBar.setVisibility(View.GONE);
                    ReceyclerPostAdapter myAdapter = new ReceyclerPostAdapter(MainActivity.this, postList);
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
        if (menuID == null) {
            recycler();
        }
    }

    private void menu_view() {
/*
        Chip chip2 = new Chip(this);
        chip2.setText("XYZ");  //chip2
        chip2.setChipBackgroundColorResource(R.color.black);
        chip2.setCloseIconVisible(true);
        chip2.setTextColor(getResources().getColor(R.color.white));
        chip2.setTextAppearance(R.style.editText);
        chipGroup.addView(chip2);
*/
        ChipGroup chipGroup = findViewById(R.id.chip_group_main);
        mFirestore.collection("Menuler")
                .whereEqualTo("dersID", "0")
                .orderBy("siralama")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            chip = new Chip(MainActivity.this);
                            chip.setText("Son Eklenenler");
                            //chip.setChipBackgroundColorResource(R.color.teal_200);
                            chip.setTextSize(8);
                            chip.setCheckable(true);
                            chip.setCloseIconVisible(false);
                            chip.setTextColor(Color.BLACK);
                            chip.setTextAppearance(R.style.editText);
                            chipGroup.addView(chip);
                            chip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Chip menu = (Chip) v;
                                    String menuName = menu.getText().toString();
                                    String dersID = (String) menu.getTag();
                                    /*
                                    Intent dersActivity = new Intent(MainActivity.this, DersActivity.class);
                                    dersActivity.putExtra("dersID", dersID);
                                    dersActivity.putExtra("menuName", menuName);
                                    dersActivity.putExtra("menuSoruSayisi", menuSoruSayisi);
                                    startActivity(dersActivity);
                                     */
                                    recycler();
                                }
                            });

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                menuName = document.getString("menu");
                                menuID = document.getId();
                                menuSoruSayisi = Integer.parseInt(document.get("soru_sayisi").toString());
                                int menu_siralamasi = Integer.parseInt(document.get("siralama").toString());

                                chip = new Chip(MainActivity.this);
                                chip.setText(menuName + " (" + String.valueOf(menuSoruSayisi) + ")");
                                //chip.setChipBackgroundColorResource(R.color.teal_200);
                                chip.setTextSize(8);
                                chip.setCheckable(true);
                                //chip.setId(menu_siralamasi);
                                chip.setTag(menuID);
                                chip.setCloseIconVisible(false);
                                chip.setTextColor(Color.BLACK);
                                chip.setTextAppearance(R.style.editText);
                                chipGroup.addView(chip);

                                chip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Chip menu = (Chip) v;
                                        menuName = menu.getText().toString();
                                        dersName = menu.getText().toString();
                                        String dersID = (String) menu.getTag();
                                        Intent dersActivity = new Intent(MainActivity.this, DersActivity.class);
                                        dersActivity.putExtra("dersID", dersID);
                                        dersActivity.putExtra("menuName", menuName);
                                        dersActivity.putExtra("menuSoruSayisi", menuSoruSayisi);
                                        //startActivity(dersActivity);


                                        recyclerDers(dersID);

                                    }
                                });

                            }
                        }
                    }
                });
    }
}


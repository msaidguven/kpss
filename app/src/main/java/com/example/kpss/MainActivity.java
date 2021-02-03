package com.example.kpss;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    private ArrayList<MenuAnaliz> dersler;

    private GridLayoutManager gridLayoutManager;
    ArrayList<Post> postList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerMenuAdapter recyclerMenuAdapter;
    private RecyclerMenuAdapter.RecyclerViewClickListener clickListener;


    private FirebaseAuth mAuth;
    private TextView textView_userName;
    private TextView btnLogin;
    private TextView btnRegister;
    private ImageView imageView;
    private FirebaseFirestore mFirestore;
    private Toolbar mToolbar;

    MainAdapter adapter;

    String userName;
    String user_image;
    String postID, post_image;


    @Override    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Çıkış Yapmak istiyor musunuz?");
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                // Evet'e basılınca yapılacak işlemleri yazınız
                finish();

            }
        });
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                // Hayır'a baslınca yapılacak işmeleri yazınız
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = new ArrayList<>();





        init();
        recycler();

        mFirestore.collection("Menuler").document("0Qa3g1rVY79Fuo5bBqUM")
                .collection("asdasd").document("dede")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userNamezxc = task.getResult().getString("gj");
                    Toast.makeText(getApplicationContext(), userNamezxc, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {



        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        textView_userName = findViewById(R.id.textView_userName);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        imageView = findViewById(R.id.imageView);
        mToolbar = findViewById(R.id.mainToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sorularla KPSS");

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




        recyclerView = findViewById(R.id.recycler_view);

        mFirestore.collection("Posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(30)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        post_image = document.get("post_image").toString();
                        postID = document.getId();
                        postList.add(new Post(postID, post_image));
                    }


                    adapter = new MainAdapter(MainActivity.this, postList);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);

                }
            }
        });
    }

    private void recycler1() {

        dersler = new ArrayList<>();
        setOnClickListener();
        recyclerMenuAdapter = new RecyclerMenuAdapter(dersler, clickListener);
        recyclerView.setAdapter(recyclerMenuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirestore.collection("Menuler")
                .whereEqualTo("dersID", "0")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String menuID = document.getId();
                                String menuName = document.getString("menu");
                                int menuSoruSayisi = Integer.parseInt(document.get("soru_sayisi").toString());
                                dersler.add(new MenuAnaliz(menuID, menuName, menuSoruSayisi));

                            }
                            recyclerMenuAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void setOnClickListener(){
        clickListener = new RecyclerMenuAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent konularActivity = new Intent(MainActivity.this, KonularActivity.class);
                konularActivity.putExtra("dersID", dersler.get(position).getDersID());
                konularActivity.putExtra("dersName", dersler.get(position).getDersName());
                startActivity(konularActivity);
            }
        };
    }


}


package com.example.kpss;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MenuAnaliz> dersler;
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
    private ListView liste;

    String userName;
    String user_image;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        recycler();

        if (mAuth.getCurrentUser() == null) {
            //Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            //startActivity(loginIntent);
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
                        Picasso.get().load(user_image).into(imageView);
                    }
                }
            });

            String user_email = mAuth.getCurrentUser().getEmail();
            textView_userName.setText(user_email);
            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        }


        mFirestore.collection("Menuler").document("0Qa3g1rVY79Fuo5bBqUM")
                .collection("asdasd").document("dede")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    userName = task.getResult().getString("gj");
                    Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();
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
        getSupportActionBar().setIcon(R.drawable.icon_setting);
        getSupportActionBar().setSubtitle(userName);

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
        return true;
    }


    private void recycler() {

        recyclerView = findViewById(R.id.recycler_view);
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


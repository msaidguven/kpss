package com.example.kpss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {
    private ArrayList<MenuAnaliz> dersler;
    private RecyclerView recyclerView;
    private RecyclerMenuAdapter recyclerMenuAdapter;
    private RecyclerMenuAdapter.RecyclerViewClickListener clickListener;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        init();

        recycler();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        dersler = new ArrayList<>();
        setOnClickListener();
        recyclerMenuAdapter = new RecyclerMenuAdapter(dersler, clickListener);
        recyclerView.setAdapter(recyclerMenuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    private void recycler() {

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
                Intent konularActivity = new Intent(RecyclerActivity.this, KonularActivity.class);
                konularActivity.putExtra("dersID", dersler.get(position).getDersID());
                konularActivity.putExtra("dersName", dersler.get(position).getDersName());
                startActivity(konularActivity);
            }
        };
    }
}

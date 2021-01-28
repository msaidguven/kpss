package com.example.kpss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class KonularActivity extends AppCompatActivity {

    private ArrayList<MenuAnaliz> dersler;
    private RecyclerView recyclerView;
    private RecyclerMenuAdapter recyclerMenuAdapter;
    private RecyclerMenuAdapter.RecyclerViewClickListener clickListener;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Toolbar mToolbar;
    ListView listViewKonular;
    int menuSoruSayisi;
    String dersID;
    String userID;
    String getDersName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konular);

        mToolbar = findViewById(R.id.mainToolbar);
        //listViewKonular = findViewById(R.id.listViewKonular);

        Intent intent = getIntent();
        dersID = intent.getStringExtra("dersID");
        getDersName = intent.getStringExtra("dersName");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getDersName);
        getSupportActionBar().setIcon(R.drawable.icon_setting);



        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //userID = mAuth.getCurrentUser().getUid();
        recycler();
        //listView();
    }

    private void listView() {
        List<String> dersName = new ArrayList<String>();
        List<String> ders_id = new ArrayList<>();
        mFirestore.collection("Menuler").whereEqualTo("dersID", dersID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.get("menu").toString();
                        String ss = document.get("soru_sayisi").toString();
                        ders_id.add(document.getId());
                        dersName.add(name +" "+ ss);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(KonularActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, dersName);
                    listViewKonular.setAdapter(adapter);
                    listViewKonular.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent konularActivity = new Intent(KonularActivity.this, PostActivity.class);
                            konularActivity.putExtra("dersID", dersID);
                            konularActivity.putExtra("konuID", ders_id.get(position));
                            konularActivity.putExtra("konuName", dersName.get(position));
                            startActivity(konularActivity);
                        }
                    });
                }
            }
        });
    }

    private void recycler() {

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
    }

    private void setOnClickListener(){
        clickListener = new RecyclerMenuAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent konularActivity = new Intent(KonularActivity.this, PostActivity.class);
                konularActivity.putExtra("dersID", dersID);
                konularActivity.putExtra("konuID", dersler.get(position).getDersID());
                konularActivity.putExtra("dersName", getDersName);
                konularActivity.putExtra("konuName", dersler.get(position).getDersName());
                konularActivity.putExtra("menuSoruSayisi", dersler.get(position).getSoruSayisi());
                startActivity(konularActivity);
            }
        };
    }

}
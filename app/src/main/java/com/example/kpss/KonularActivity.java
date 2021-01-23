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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class KonularActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Toolbar mToolbar;
    ListView listViewKonular;

    String dersID;
    String userID;
    String getDersName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konular);

        mToolbar = findViewById(R.id.mainToolbar);
        listViewKonular = findViewById(R.id.listViewKonular);

        Intent intent = getIntent();
        dersID = intent.getStringExtra("dersID");
        getDersName = intent.getStringExtra("dersName");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getDersName);
        getSupportActionBar().setIcon(R.drawable.icon_setting);



        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //userID = mAuth.getCurrentUser().getUid();
        listView();
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

/*

    private void buton_olustur(){
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.ly_id); //a constraint layout pre-made in design view
        mFirestore.collection("Menuler").whereEqualTo("dersID", dersID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //List<String> ders = new ArrayList<>();
                    //List<String> ders_id = new ArrayList<>();

                    //ders_id.add("");
                    //ders.add("Ders Seçin");
                    int x = 0;
                    int y = -135;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //ders_id.add(document.getId());
                        //ders.add(document.getString("menu"));

                        Button btn = new Button(KonularActivity.this);

                        btn.setPadding(5,5,5,5);
                        btn.setText(document.getString("menu"));
                        btn.setBackgroundResource(R.drawable.bg_menu);

                        btn.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
                        //btn.set
                        btn.setId(View.generateViewId());
                        constraintLayout.addView(btn);
                        //x = 10;
                        y += 135;
                        ConstraintSet set = new ConstraintSet();
                        set.clone(constraintLayout);
                        set.connect(btn.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, x);
                        set.connect(btn.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, y);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent konularActivity = new Intent(KonularActivity.this, PostActivity.class);
                                konularActivity.putExtra("dersID", dersID);
                                konularActivity.putExtra("konuID", document.getId());
                                konularActivity.putExtra("konuName", document.getString("menu"));
                                startActivity(konularActivity);
                                //Toast.makeText(getApplicationContext(), String.valueOf(btn.getId()), Toast.LENGTH_LONG).show();
                            }
                        });
                        set.applyTo(constraintLayout);
                    }

                }
            }
        });
    }
    */
}
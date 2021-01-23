package com.example.kpss;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    LinearLayout fragment_container_view_tag;
    ImageView ImageView_post_image;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Toolbar postToolbar;
    String d_cevap;
    String postID;
    String btn_getText;
    String getDersName;
    String getKonuName;
    String dersID;
    String konuID;
    String userID;
    String post_image;
    String analizCevapIncrement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FragmentSonuc fragmentSonuc = new FragmentSonuc();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction = transaction.add(R.id.fragment_container_view_tag, fragmentSonuc, "Fragment Sonucu");
        transaction.commit();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        dersID = intent.getStringExtra("dersID");
        konuID = intent.getStringExtra("konuID");
        getKonuName = intent.getStringExtra("konuName");
        getDersName = intent.getStringExtra("dersName");
        userID = mAuth.getCurrentUser().getUid();

        init();


        mFirestore.collection("Posts")
                .whereEqualTo("konuID", konuID)
                .orderBy("postUnic_autoIncrement")
                .startAt(1)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                postID = document.getId();
                                post_image = document.getString("post_image");
                                d_cevap = document.getString("d_cevap");
                                //String post_image = task.getResult().getString("post_image");
                                Picasso.get().load(post_image).into(ImageView_post_image);
                            }
                            if (task.getResult().size() == 0) {
                                Toast.makeText(getApplicationContext(), "Soru Bulunamadı", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void init() {
        ImageView_post_image = findViewById(R.id.ImageView_post_image);
        fragment_container_view_tag = findViewById(R.id.fragment_container_view_tag);
        postToolbar = findViewById(R.id.postToolbar);

        setSupportActionBar(postToolbar);
        getSupportActionBar().setTitle(getDersName);
        getSupportActionBar().setIcon(R.drawable.icon_setting);
        getSupportActionBar().setSubtitle(getKonuName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }

    public void secenekClick(View v) {
        Button vee = (Button) v;
        btn_getText = vee.getText().toString();

        Button btnA = findViewById(R.id.secenkA);
        Button btnB = findViewById(R.id.secenkB);
        Button btnC = findViewById(R.id.secenkC);
        Button btnD = findViewById(R.id.secenkD);
        Button btnE = findViewById(R.id.secenkE);

        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        btnE.setEnabled(false);

        if (btn_getText.equals(d_cevap)) {
            Toast.makeText(getApplicationContext(), "Tebrikler doğru cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.GREEN);
            analizCevapIncrement = "dogru_sayisi";
        } else {
            Toast.makeText(getApplicationContext(), "Yanlış cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.RED);
            analizCevapIncrement = "yanlis_sayisi";
        }

        if (mAuth.getCurrentUser() == null) {

            HashMap<String, Object> postAnaliz = new HashMap<>();
            postAnaliz.put("postID", postID);
            postAnaliz.put("userID", userID);
            postAnaliz.put(btn_getText, FieldValue.increment(1));
            postAnaliz.put("time", FieldValue.serverTimestamp());
            String uyeSoruAnaliz_docName = userID + postID;
            DocumentReference uyeSoruAnaliz = mFirestore.collection("uyeSoruAnaliz").document(uyeSoruAnaliz_docName);
            uyeSoruAnaliz.set(postAnaliz, SetOptions.merge());


            HashMap<String, Object> konuAnaliz = new HashMap<>();
            konuAnaliz.put("konuID", konuID);
            konuAnaliz.put("userID", userID);
            konuAnaliz.put(analizCevapIncrement, FieldValue.increment(1));
            konuAnaliz.put("time", FieldValue.serverTimestamp());
            String uyeKonuAnaliz_docName = userID + konuID;
            DocumentReference uyeKonuAnaliz = mFirestore.collection("uyeKonuAnaliz").document(uyeKonuAnaliz_docName);
            uyeKonuAnaliz.set(konuAnaliz, SetOptions.merge());

            HashMap<String, Object> dersAnaliz = new HashMap<>();
            dersAnaliz.put("dersID", dersID);
            dersAnaliz.put("userID", userID);
            dersAnaliz.put(analizCevapIncrement, FieldValue.increment(1));
            dersAnaliz.put("time", FieldValue.serverTimestamp());
            String uyeDersAnaliz_docName = userID + dersID;
            DocumentReference uyeDersAnaliz = mFirestore.collection("uyeDersAnaliz").document(uyeDersAnaliz_docName);
            uyeDersAnaliz.set(dersAnaliz, SetOptions.merge());

            DocumentReference postCevap_increment = mFirestore.collection("Posts").document(postID);
            postCevap_increment.update(btn_getText, FieldValue.increment(1));

        }

    }
}
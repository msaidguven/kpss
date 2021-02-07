package com.example.kpss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private String vee_id;
    private ProgressDialog dialog;
    private Toolbar postToolbar;




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

        postToolbar = findViewById(R.id.postToolbar);

        setSupportActionBar(postToolbar);
        getSupportActionBar().setTitle("Türkçe");
        getSupportActionBar().setIcon(R.drawable.icon_setting);
        getSupportActionBar().setSubtitle("Dil Bilgisi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                        post_image = document.get("post_image").toString();
                        postID = document.getId();
                        time = document.get("time").toString();
                        d_cevap = document.get("d_cevap").toString();
                        time1 = document.get("time1") == null ? "0" : document.get("time1").toString();
                        konuID = document.get("konuID").toString();
                        postList.add(new PostModel(postID, post_image, time, time1, konuID, d_cevap));
                    }
                    Toast.makeText(getApplicationContext(), String.valueOf(task.getResult().size()), Toast.LENGTH_SHORT).show();
                    ViewPagerAdapter adapter = new ViewPagerAdapter(PostListActivity.this, postList);
                    viewPager.setAdapter(adapter);
                }
            }
        });
    }


    public void secenekClick (View v){

        Button vee = (Button) v;
        vee_id = vee.getText().toString();


        Toast.makeText(getApplicationContext(), vee_id, Toast.LENGTH_SHORT).show();
/*
        if (vee_id.equals(d_cevap)){
            Toast.makeText(getApplicationContext(), vee_id, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "yanlış", Toast.LENGTH_SHORT).show();
        }
*/



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        dialog.setTitle("Lütfen Bekleyin");
        dialog.setMessage("Siliniyor");
        dialog.closeOptionsMenu();
        dialog.setOnDismissListener(DialogInterface::cancel);
        dialog.show();

        /*
        storageReference = FirebaseStorage.getInstance().getReference()
        storageReference = mFirebaseStorage.getReferenceFromUrl(mImageUrl);
        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(mImageUrl);
*/


        if (item.getItemId() == R.id.menu_deletePost) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Bu soruyu silmek istiyor musunuz?");
            builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference storageReference = firebaseStorage.getReferenceFromUrl(post_image);
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFirestore.collection("Posts").document(postID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Soru Silindi", Toast.LENGTH_SHORT);
                                            Intent intent = new Intent(PostListActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error deleting document", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
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

        if (item.getItemId() == R.id.menu_addPost) {
            //Intent settingsIntent = new Intent(MainActivity.this, AddPost.class);
            //startActivity(settingsIntent);
        }

        return true;
    }




}
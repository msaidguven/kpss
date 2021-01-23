package com.example.kpss;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        tanimla();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        listView();


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

        //buton_olustur();


    }


    private void tanimla() {
        textView_userName = findViewById(R.id.textView_userName);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        imageView = findViewById(R.id.imageView);
        mToolbar = findViewById(R.id.mainToolbar);
        liste = findViewById(R.id.listViewDersler);

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

        if (item.getItemId() == R.id.menu_addCategory) {
            Intent settingsIntent = new Intent(MainActivity.this, AddCategoryActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

    private void buton_olustur() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout_menuler); //a constraint layout pre-made in design view
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        mFirestore.collection("Menuler").whereEqualTo("dersID", "0").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> ders = new ArrayList<>();
                    List<String> ders_id = new ArrayList<>();
                    ders_id.add("");
                    ders.add("Ders Seçin");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ders_id.add(document.getId());
                        ders.add(document.getString("menu"));
                        int buttonStyle = R.style.button;
                        //int buttonStyle = R.style.Widget_AppCompat_Light_ActionButton;

                        Button btn = new Button(MainActivity.this);

                        //btn.setBackgroundResource(R.drawable.common_google_signin_btn_text_dark_normal);
                        btn.setBackgroundResource(R.drawable.bg_menu2);
                        btn.setPadding(5, 10, 5, 15);
                        btn.setText(document.getString("menu"));
                        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                        btn.setId(View.generateViewId());
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent konularActivity = new Intent(MainActivity.this, KonularActivity.class);
                                konularActivity.putExtra("dersID", document.getId());
                                konularActivity.putExtra("dersName", document.getString("menu"));
                                finish();
                                startActivity(konularActivity);
                                //Toast.makeText(getApplicationContext(), String.valueOf(btn.getId()), Toast.LENGTH_LONG).show();
                            }
                        });
                        linearLayout.addView(btn);
                    }
                }
            }
        });
    }

    private void listView() {

        List<ListViewMenu> menuler = new ArrayList<ListViewMenu>();
        List<String> dersName = new ArrayList<String>();
        List<String> ders_id = new ArrayList<>();
        ;
        mFirestore.collection("Menuler")
                .whereEqualTo("dersID", "0")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String name = document.get("menu").toString();
                                String ss = document.get("soru_sayisi").toString();

                                ders_id.add(document.getId());
                                dersName.add(name + " " + ss);

                                String menuID = document.getId();
                                String menuName = document.getString("menu");
                                int menuSoruSayisi = 2;
                                int uyeninDogruCevapSayisi = 0;
                                int uyeninYanlisCevapSayisi = 0;
                                menuler.add(new ListViewMenu(menuID, menuName, menuSoruSayisi, uyeninDogruCevapSayisi, uyeninYanlisCevapSayisi));
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, dersName);
                                liste.setAdapter(adapter);
                                liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent konularActivity = new Intent(MainActivity.this, KonularActivity.class);
                                        konularActivity.putExtra("dersID", ders_id.get(position));
                                        konularActivity.putExtra("dersName", dersName.get(position));
                                        startActivity(konularActivity);
                                    }
                                });
/*
                        OzelAdaptor menuAdaptor = new OzelAdaptor(MainActivity.this, menuler);
                        liste.setAdapter(menuAdaptor);
                        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String menu_id = menuler.get(position).getMenuID();
                                String menu_name = menuler.get(position).getMenuName();
                                Toast.makeText(getApplicationContext(), menu_id, Toast.LENGTH_SHORT).show();
                                Intent konularActivity = new Intent(MainActivity.this, KonularActivity.class);
                                konularActivity.putExtra("dersID", menu_id);
                                konularActivity.putExtra("dersName", menu_name);
                                startActivity(konularActivity);
                            }
                        });
                         */
                            }
                        }
                    }
                });
    }

}


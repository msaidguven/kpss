package com.example.kpss;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostActivity extends AppCompatActivity {


    LinearLayout linearLayout_uyeSoruAnaliz;
    private ProgressDialog dialog;

    private Button btnA, btnB, btnC, btnD, btnE;
    private Button userCevap_A, userCevap_B, userCevap_C, userCevap_D, userCevap_E;
    private ImageView buttonOncekiSoru, buttonSonrakiSoru;
    private TextView textView_dogru, textView_yanlis, textView_gosterge;
    LinearLayout fragment_container_view_tag;
    ImageView ImageView_post_image;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Toolbar postToolbar;
    private View post_setting;
    int A, B, C, D, E;
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
    String time;
    private int uyeninCozduguSonSoru;
    private int menuSoruSayisi;

    int startAfter = 0;

    AnyChartView anyChartView;
    String[] secenekler = {"A", "B", "C", "D", "E"};
    int[] cevaplar = {0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        dersID = intent.getStringExtra("dersID");
        konuID = intent.getStringExtra("konuID");
        getKonuName = intent.getStringExtra("konuName");
        getDersName = intent.getStringExtra("dersName");
        menuSoruSayisi = intent.getIntExtra("menuSoruSayisi", menuSoruSayisi);

        init();
        soruGetir();



        buttonOncekiSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startAfter == 0){
                    Toast.makeText(getApplicationContext(), "İlk Sorudasın", Toast.LENGTH_SHORT).show();
                }else {
                    startAfter--;
                    soruGetir();
                }
                //Intent intent = getIntent();
                //finish();
                //startActivity(intent);
            }
        });

        buttonSonrakiSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startAfter+1 == menuSoruSayisi){
                    Toast.makeText(getApplicationContext(), "Son Sorudasın", Toast.LENGTH_SHORT).show();
                }else {
                    startAfter++;
                    soruGetir();
                }
                //Intent intent = getIntent();
                //finish();
                //startActivity(intent);
            }
        });

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }


    }

    private void soruGetir() {

        if(startAfter+1 == menuSoruSayisi){
            buttonSonrakiSoru.setEnabled(false);
        }else {
            buttonSonrakiSoru.setEnabled(true);
        }
        if(startAfter == 0){
            buttonOncekiSoru.setEnabled(false);
        }else {
            buttonOncekiSoru.setEnabled(true);
        }

        textView_gosterge.setText(startAfter+1 +" / " + menuSoruSayisi);
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);
        btnE.setEnabled(true);

        btnA.setBackgroundColor(Color.BLUE);
        btnB.setBackgroundColor(Color.BLUE);
        btnC.setBackgroundColor(Color.BLUE);
        btnD.setBackgroundColor(Color.BLUE);
        btnE.setBackgroundColor(Color.BLUE);

        post_setting.setVisibility(View.GONE);

        dialog.setTitle("lütfen Bekleyiniz");
        dialog.setMessage("Soru yükleniyor");
        dialog.show();

        mFirestore.collection("uyeKonuAnaliz").document(userID + konuID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().get("uyeninCozduguSonSoru") != null) {
                        uyeninCozduguSonSoru = Integer.parseInt(task.getResult().get("uyeninCozduguSonSoru").toString());
                    } else {
                        uyeninCozduguSonSoru = 0;
                    }

                    mFirestore.collection("Posts")
                            .whereEqualTo("konuID", konuID)
                            .orderBy("postUnic_autoIncrement")
                            //.startAt(uyeninCozduguSonSoru + 1)
                            .startAfter(startAfter) //acaba aynı şey mi yukardaki ile
                            .limit(1)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                    if (value.size() == 0) {
                                        Toast.makeText(getApplicationContext(), "Soru Bulunamadı", Toast.LENGTH_SHORT).show();
                                    } else {
                                        for (QueryDocumentSnapshot doc : value) {
                                            postID = doc.getId();
                                            post_image = doc.getString("post_image");
                                            d_cevap = doc.getString("d_cevap");
                                            A = Integer.parseInt(doc.get("A").toString());
                                            B = Integer.parseInt(doc.get("B").toString());
                                            C = Integer.parseInt(doc.get("C").toString());
                                            D = Integer.parseInt(doc.get("D").toString());
                                            E = Integer.parseInt(doc.get("E").toString());
                                            Picasso.get().load(post_image).into(ImageView_post_image);
                                            uyeninCozduguSonSoru = Integer.parseInt(doc.get("postUnic_autoIncrement").toString());
                                            int top = A + B + C + D + E;
                                            if (top == 0) {
                                                top = 1;
                                            }

                                            setupPieChart();
                                        }

                                        dialog.dismiss();

                                    }
                                }
                            });
                }
            }
        });
    }

    private void init() {

        anyChartView = findViewById(R.id.any_chart_view);

        ImageView_post_image = findViewById(R.id.ImageView_post_image);
        fragment_container_view_tag = findViewById(R.id.fragment_container_view_tag);
        postToolbar = findViewById(R.id.postToolbar);

        dialog = new ProgressDialog(this);

        post_setting = findViewById(R.id.post_setting);
        textView_dogru = findViewById(R.id.textView_dogru);
        textView_yanlis = findViewById(R.id.textView_yanlis);
        textView_gosterge = findViewById(R.id.textView_gosterge);

        buttonOncekiSoru = findViewById(R.id.buttonOncekiSoru);
        buttonSonrakiSoru = findViewById(R.id.buttonSonrakiSoru);

        btnA = findViewById(R.id.secenkA);
        btnB = findViewById(R.id.secenkB);
        btnC = findViewById(R.id.secenkC);
        btnD = findViewById(R.id.secenkD);
        btnE = findViewById(R.id.secenkE);



        userCevap_A = findViewById(R.id.userCevap_A);
        userCevap_B = findViewById(R.id.userCevap_B);
        userCevap_C = findViewById(R.id.userCevap_C);
        userCevap_D = findViewById(R.id.userCevap_D);
        userCevap_E = findViewById(R.id.userCevap_E);
        linearLayout_uyeSoruAnaliz = findViewById(R.id.linearLayout_uyeSoruAnaliz);



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

    public void setupPieChart(){
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("A", A));
        data.add(new ValueDataEntry("B", B));
        data.add(new ValueDataEntry("C", C));
        data.add(new ValueDataEntry("D", D));
        data.add(new ValueDataEntry("E", E));

        pie.data(data);

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }

    public void secenekClick(View v) {
        Button vee = (Button) v;
        btn_getText = vee.getText().toString();

        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        btnE.setEnabled(false);

        if (btn_getText.equals(d_cevap)) {
            Toast.makeText(getApplicationContext(), "Tebrikler doğru cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.GREEN);
            textView_dogru.setText("Tebrikler doğru vecap verdiniz");
            textView_dogru.setVisibility(View.VISIBLE);
            analizCevapIncrement = "dogru_sayisi";
        } else {
            Toast.makeText(getApplicationContext(), "Yanlış cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.RED);
            textView_yanlis.setText("Yanlış cevap verdiniz. Doğru cevap " + d_cevap + " olacak.");
            textView_yanlis.setVisibility(View.VISIBLE);
            analizCevapIncrement = "yanlis_sayisi";
        }

        if (mAuth.getCurrentUser() != null) {

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
            konuAnaliz.put("uyeninCozduguSonSoru", uyeninCozduguSonSoru);
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

            //UyeSoruAnaliz uyeninCevaplari = new UyeSoruAnaliz(uyeSoruAnaliz_docName);


            mFirestore.collection("uyeSoruAnaliz").document(uyeSoruAnaliz_docName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        A = document.get("A") == null ? 0 : Integer.parseInt(task.getResult().get("A").toString());
                        B = document.get("B") == null ? 0 : Integer.parseInt(task.getResult().get("B").toString());
                        C = document.get("C") == null ? 0 : Integer.parseInt(task.getResult().get("C").toString());
                        D = document.get("D") == null ? 0 : Integer.parseInt(task.getResult().get("D").toString());
                        E = document.get("E") == null ? 0 : Integer.parseInt(task.getResult().get("E").toString());

                        linearLayout_uyeSoruAnaliz.setVisibility(View.VISIBLE);

                        int toplam = A + B + C + D + E;
                        if (toplam == 0) {
                            toplam = 1;
                        }
                        userCevap_A.setText(String.valueOf(A));
                        userCevap_B.setText(String.valueOf(B));
                        userCevap_C.setText(String.valueOf(C));
                        userCevap_D.setText(String.valueOf(D));
                        userCevap_E.setText(String.valueOf(E));

                        ViewGroup.LayoutParams paramsA = userCevap_A.getLayoutParams();
                        ViewGroup.LayoutParams paramsB = userCevap_B.getLayoutParams();
                        ViewGroup.LayoutParams paramsC = userCevap_C.getLayoutParams();
                        ViewGroup.LayoutParams paramsD = userCevap_D.getLayoutParams();
                        ViewGroup.LayoutParams paramsE = userCevap_E.getLayoutParams();

                        paramsA.width = ((300 * A) / toplam);
                        paramsB.width = ((300 * B) / toplam);
                        paramsC.width = ((300 * C) / toplam);
                        paramsD.width = ((300 * D) / toplam);
                        paramsE.width = ((300 * E) / toplam);

                    }
                }
            });

        }


        post_setting.setVisibility(View.VISIBLE);

    }
}
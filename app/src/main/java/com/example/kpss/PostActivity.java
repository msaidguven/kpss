package com.example.kpss;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/*
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.core.ui.Center;
*/


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    private ImageView buttonOncekiSoru, buttonSonrakiSoru;
    private TextView textView_aciklama, textView_gosterge;
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
    double yuzde;
    double toplam;

    int startAfter = 0;

    private int[] yData;
    private String[] xData = new String[]{"A", "B", "C", "D", "E"};
    private List xDatas = new ArrayList();

    com.github.mikephil.charting.charts.PieChart pieChart;
    com.github.mikephil.charting.charts.PieChart pieChart1;

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
                if (startAfter == 0) {
                    Toast.makeText(getApplicationContext(), "İlk Sorudasın", Toast.LENGTH_SHORT).show();
                } else {
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
                if (startAfter + 1 == menuSoruSayisi) {
                    Toast.makeText(getApplicationContext(), "Son Sorudasın", Toast.LENGTH_SHORT).show();
                } else {
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

        if (startAfter + 1 == menuSoruSayisi) {
            buttonSonrakiSoru.setEnabled(false);
        } else {
            buttonSonrakiSoru.setEnabled(true);
        }
        if (startAfter == 0) {
            buttonOncekiSoru.setEnabled(false);
        } else {
            buttonOncekiSoru.setEnabled(true);
        }

        textView_gosterge.setText(startAfter + 1 + " / " + menuSoruSayisi);
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

        dialog.setTitle("Lütfen Bekleyin");
        dialog.setMessage("Soru yükleniyor");
        dialog.closeOptionsMenu();
        dialog.setOnDismissListener(DialogInterface::cancel);
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

                                            yData = new int[]{A, B, C, D, E};
                                            setupPieChart();

                                            toplam = A + B + C + D + E;


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

        pieChart = findViewById(R.id.pie_chart);
        pieChart1 = findViewById(R.id.pie_chart1);


        ImageView_post_image = findViewById(R.id.ImageView_post_image);
        fragment_container_view_tag = findViewById(R.id.fragment_container_view_tag);
        postToolbar = findViewById(R.id.postToolbar);

        dialog = new ProgressDialog(this);

        post_setting = findViewById(R.id.post_setting);
        textView_aciklama = findViewById(R.id.textView_aciklama);
        textView_gosterge = findViewById(R.id.textView_gosterge);

        buttonOncekiSoru = findViewById(R.id.buttonOncekiSoru);
        buttonSonrakiSoru = findViewById(R.id.buttonSonrakiSoru);

        btnA = findViewById(R.id.secenkA);
        btnB = findViewById(R.id.secenkB);
        btnC = findViewById(R.id.secenkC);
        btnD = findViewById(R.id.secenkD);
        btnE = findViewById(R.id.secenkE);


        linearLayout_uyeSoruAnaliz = findViewById(R.id.linearLayout_uyeSoruAnaliz);

        setSupportActionBar(postToolbar);
        getSupportActionBar().setTitle(getDersName);
        getSupportActionBar().setIcon(R.drawable.icon_setting);
        getSupportActionBar().setSubtitle(getKonuName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }


    public void setupPieChart() {
        pieChart.getDescription().setText("Doğru Cevap : " + d_cevap);
        pieChart.getDescription().setTextSize(16f);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        //pieChart.setCenterText(d_cevap);
        // pieChart.setCenterTextColor(R.color.colorPrimaryDark);
        //pieChart.setCenterTextSize(20f);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(3000);
        pieChart.animateX(3000);
        addDataset();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float secili = Float.parseFloat(e.toString().substring(17));
                int index = 0;
                for (int i = 0; i < yData.length; i++) {
                    if (yData[i] == secili) {
                        index = i;
                    }
                }
                Toast.makeText(getApplicationContext(), "" + xData[index], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }


    public void setupPieChart1() {
        pieChart1.getDescription().setText("Doğru Cevap : " + d_cevap);
        pieChart1.getDescription().setTextSize(16f);
        pieChart1.setRotationEnabled(true);
        pieChart1.setHoleRadius(25f);
        pieChart1.setTransparentCircleAlpha(0);
        //pieChart.setCenterText(d_cevap);
        // pieChart.setCenterTextColor(R.color.colorPrimaryDark);
        //pieChart.setCenterTextSize(20f);
        pieChart1.setDrawEntryLabels(false);
        pieChart1.animateY(3000);
        pieChart1.animateX(3000);
        addDataset1();
        pieChart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float secili = Float.parseFloat(e.toString().substring(17));
                int index = 0;
                for (int i = 0; i < yData.length; i++) {
                    if (yData[i] == secili) {
                        index = i;
                    }
                }
                Toast.makeText(getApplicationContext(), "" + xData[index], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }


    public void secenekClick(View v) {
        Button vee = (Button) v;
        btn_getText = vee.getText().toString();

        if (btn_getText.equals(d_cevap)) {
            Toast.makeText(getApplicationContext(), "Tebrikler doğru cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.GREEN);
            textView_aciklama.setTextColor(Color.GREEN);
            textView_aciklama.setText("Tebrikler doğru vecap verdiniz");
            analizCevapIncrement = "dogru_sayisi";
            istatistikleriKaydet();
        } else if (d_cevap.equals("Bilinmiyor")) {
            textView_aciklama.setTextColor(Color.BLUE);
            textView_aciklama.setText("Bu soruya doğru cevap eklenmemiş. Çözüm ekleyerek doğru cevabın bulunmasına yardım edebilirsin.");
        } else {
            Toast.makeText(getApplicationContext(), "Yanlış cevap", Toast.LENGTH_SHORT).show();
            vee.setBackgroundColor(Color.RED);
            textView_aciklama.setTextColor(Color.RED);
            textView_aciklama.setText("Yanlış cevap verdiniz. Doğru cevap " + d_cevap + " olacak.");
            analizCevapIncrement = "yanlis_sayisi";
            istatistikleriKaydet();
        }

        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        btnE.setEnabled(false);

        DocumentReference postCevap_increment = mFirestore.collection("Posts").document(postID);
        postCevap_increment.update(btn_getText, FieldValue.increment(1));

        post_setting.setVisibility(View.VISIBLE);

    }


    private void istatistikleriKaydet() {
        if (mAuth.getCurrentUser() != null) {

            linearLayout_uyeSoruAnaliz.setVisibility(View.VISIBLE);

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
                        setupPieChart1();

                        /*
                        String[] textArray = {
                                "A = " + String.valueOf(A),
                                "B = " + String.valueOf(B),
                                "C = " + String.valueOf(C),
                                "D = " + String.valueOf(D),
                                "E = " + String.valueOf(E),
                        };

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_sonuclariGoster);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                        TextView textView1 = new TextView(PostActivity.this);
                        textView1.setText("Bu soru " + String.valueOf(toplam) + " defa çözüldü.");
                        textView1.setTextAppearance(R.style.textView);
                        textView1.setBackgroundColor(Color.WHITE);
                        textView1.setTextColor(Color.BLACK);
                        textView1.setPadding(5, 5, 5, 5);
                        textView1.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        linearLayout.addView(textView1);

                        for (int i = 0; i < textArray.length; i++) {
                            TextView textView = new TextView(PostActivity.this);
                            if (d_cevap.equals(xData[i]))
                                textView.setTextColor(Color.GREEN);
                            else
                                textView.setTextColor(Color.RED);
                            yuzde = (100 * yData[i]) / toplam;
                            textView.setText(textArray[i]);
                            textView.setTextAppearance(R.style.textView);
                            textView.setBackgroundColor(Color.WHITE);
                            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            linearLayout.addView(textView);
                        }
                        */

                    }
                }
            });
        }
    }


    private void addDataset() {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntry.add(new PieEntry(yData[i], xData[i]));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntry.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(13f);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        pieDataSet.setColors(colors);
        pieDataSet.setFormSize(19f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void addDataset1() {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntry.add(new PieEntry(yData[i], xData[i]));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntry.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(13f);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        pieDataSet.setColors(colors);
        pieDataSet.setFormSize(19f);
        PieData pieData = new PieData(pieDataSet);
        pieChart1.setData(pieData);
        pieChart1.invalidate();
    }


}
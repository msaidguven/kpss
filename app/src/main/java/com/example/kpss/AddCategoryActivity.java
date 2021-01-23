package com.example.kpss;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddCategoryActivity extends AppCompatActivity {

    private String turkceID = "70SCdfkm5gluV5tNLkYX";
    private String matematikID = "HYByBRktbJODnTEF1uq6";
    private String geometriID = "kqVu0PQyOQOWc2TPFAkU";
    private String tarihID = "XjB8V5JNwiKOwi37n2C0";
    private String cografyaID = "ejjFZP2PFSyqQciQGSeX";
    private String anayasaID = "3HFkX9VYPfYQUhYavveY";
    private String egitimBilimleriID = "Eqzk5GuYAyyrPl6jhiTx";
    private String aGrubuID = "fP1azSDSiz7a82jDeVwo";
    private String oabtID = "xvKWvFeNwNdlGntaxffN";
    private String mainID = "0";


    private String[] main = {
            "Türkçe",
            "Matematik",
            "Geometri",
            "Tarih",
            "Cografya",
            "Anayasa",
            "Eğitim Bilimleri",
            "ÖABT",
            "A Grubu"
    };

    private String[] turkce = {
            "Sözcük Anlamı",
            "Cümlede Anlam",
            "Paragraf",
            "Dil Bilgisi",
            "Anlatım Bozuklukları",
            "Yazım Kuralları",
            "Noktalama İşaretleri",
            "Sözel Mantık ve Akıl Yürütme"

    };

    private String[] tarih = {
            "İslamiyetten Önceki Türk Devletleri",
            "İlk Müslüman Türk Devletleri",
            "Osmanlı Devleti Siyasi",
            "Osmanlı Devleti Kültür ve Uygarlık",
            "XX.Yüzyılda Osmanlı",
            "Kurtuluş Savaşı Hazırlık Dönemi",
            "Kurtuluş Savaşı Cepheleri",
            "Devrim Tarihi",
            "Atatürk Dönemi İç ve Dış Politika",
            "Atatürk İlkeleri",
            "Çağdaş Türk ve Dünya Tarihi"
    };

    private String[] cografya = {
            "Türkiye Fiziki Coğrafyası",
            "Sanayi",
            "Ticaret",
            "Ulaşım",
            "Turizm",
            "Türkiye Bölgeler Coğrafyası",
            "Türkiye Coğrafi Konumu",
            "Türkiye’nin Yer şekilleri Su Örtüsü",
            "Türkiye’nin İklimi Ve Bitki Örtüsü",
            "Toprak Ve Doğa Çevre",
            "Türkiye’nin Beşeri Coğrafyası",
            "Türkiye Ekonomik Coğrafyası",
            "Tarım Hayvancılık Ve Orman",
            "Madenler Ve Enerji Kaynakları"
    };

    private String[] egitimBilimleri = {
            "GELİŞİM PSİKOLOJİSİ",
            "ÖĞRENME PSİKOLOJİSİ",
            "ÖĞRETİM YÖNTEM VE TEKNİKLERİ",
            "ÖLÇME VE DEĞERLENDİRME",
            "PROGRAM GELİŞTİRME",
            "REHBERLİK"
    };

    private String[] matematik = {
            "Temel Kavramlar",
            "Sayılar",
            "Bölme-Bölünebilme Kuralları",
            "Asal Çarpanlara Ayırma",
            "EBOB-EKOK",
            "Birinci Dereceden Denklemler",
            "Rasyonel Sayılar",
            "Eşitsizlikler",
            "Mutlak Değer",
            "Üslü Sayılar",
            "Köklü Sayılar",
            "Çarpanlara Ayırma",
            "Oran – Orantı",
            "Problemler",
            "Kümeler",
            "Fonskiyon İşlem",
            "Modüler Aritmetik",
            "Permütasyon – Kombinasyon – Olasılık",
            "Tablo ve Grafikler",
            "Sayısal Mantık"
    };

    private String[] geometri = {
            "Doğru Açılar",
            "Üçgende Açılar",
            "Üçgende Açı-Kenar Bağlantıları",
            "Özel Üçgenler",
            "Açıortay-Kenarortay Bağlantıları",
            "Üçgende Alan",
            "Üçgende Benzerlik",
            "Dörtgenler – Çokgenler",
            "Çember – Daire",
            "Analtik Geometri",
            "Katı Cisimler"
    };

    private String[] anayasa = {
            "Hukuka Giriş",
            "Anayasal Gelişmeler",
            "Genel Esaslar",
            "Temel Haklar",
            "Yasama",
            "Yürütme",
            "Yargı",
            "İdari Yapı",
            "Uluslararası Örgütler",
            "Güncel"
    };


    private int i = 0;


    private Toolbar mToolbar;
    private EditText catEditText;
    private EditText ornekSoru;
    private Button kaydetButton;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        catEditText = findViewById(R.id.editText_kategori);
        ornekSoru = findViewById(R.id.ornekSoru);
        kaydetButton = findViewById(R.id.btn_kaydet);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);

        mToolbar = findViewById(R.id.categoryToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Kategori Activity");


        kaydetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Kaydediliyor");
                progressDialog.setMessage("Kategori Ekleniyor");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                addCategory();

            }
        });
    }


    private void addCategory() {
        if (i < tarih.length) {
            String unite = tarih[i];
            int adet = tarih.length;
            //fireSoreKaydet("Menuler", "0");
            fireSoreKaydet("Menuler", egitimBilimleriID);
            //fireSoreGuncelle("Menu");
            progressDialog.dismiss();
        }
    }

    public void fireSoreKaydet(String koleksiyon, String parent) {
        for (String addUnit : egitimBilimleri) {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("dersID", parent);
            userMap.put("siralama", (i + 1));
            userMap.put("soru_sayisi", 0);
            userMap.put("menu", addUnit);
            mFirestore.collection(koleksiyon).document().set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), addUnit, Toast.LENGTH_LONG).show();
                    }
                }
            });
            i++;
        }
    }

    public void fireSoreGuncelle(String koleksiyon, String belge) {
        String unite = tarih[i];
        HashMap<String, Object> userUpdateMap = new HashMap<>();
        for (String addUnit : tarih) {
            userUpdateMap.put("unite:" + (i + 1), addUnit);
            mFirestore.collection(koleksiyon).document(belge).update(userUpdateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), unite, Toast.LENGTH_LONG).show();
                    }
                }
            });
            i++;
        }

    }

}
package com.example.kpss;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPost extends AppCompatActivity {

    ArrayAdapter adp;
    Spinner AcilirListe;
    Spinner spinner_ders;
    Spinner spinner_konu;
    String dCevap;
    private CircleImageView postImage;
    private Button btnResimSec;
    private Button btn_soruEkle;
    private Boolean resim_Ischeck = false;
    private Boolean dCevap_Ischeck = false;
    private Boolean konu_Ischeck = false;
    private ProgressDialog progressDialog;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private StorageReference str;
    private FirebaseFirestore mFirestore;

    private String konuID;
    private String dersID;
    private int konu_soru_sayisi_int;
    private int postUnic_autoIncrement_int;
    String user_id;
    String newImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        tanimlama();
        dersListele();
        dCevap();


        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddPost.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddPost.this);
            }
        });


        btnResimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddPost.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddPost.this);
            }
        });

        btn_soruEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dCevap();
                progressDialog.setTitle("Yükleniyor");
                progressDialog.setMessage("Soru yükleniyor, lütfen bekleyiniz");
                progressDialog.show();
                if ((konu_Ischeck) && (dCevap_Ischeck) && (resim_Ischeck)) {
                    resimYukle();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), konu_Ischeck.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), dCevap_Ischeck.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), resim_Ischeck.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                postImage.setImageURI(resultUri);
                resim_Ischeck = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "resim seçilmedi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void fireSore_soruKaydet() {
        String uniqueString = UUID.randomUUID().toString();
        user_id = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("konuID", konuID);
        userMap.put("dersID", dersID);
        userMap.put("d_cevap", dCevap);
        userMap.put("post_image", newImageUri);
        userMap.put("userID", user_id);
        userMap.put("A", 0);
        userMap.put("B", 0);
        userMap.put("C", 0);
        userMap.put("D", 0);
        userMap.put("E", 0);
        //userMap.put("unique", System.currentTimeMillis());
        userMap.put("postUnic_autoIncrement", FieldValue.increment(1));
        userMap.put("time", FieldValue.serverTimestamp());
        //Date now = new Date();
        //long timestamp = now.getTime();
        //Toast.makeText(getApplicationContext(), String.valueOf(timestamp), Toast.LENGTH_SHORT).show();
        userMap.put("time1", new Date().getTime());


        mFirestore.collection("Posts").document().set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Soru eklendi", Toast.LENGTH_LONG).show();

                    DocumentReference konuSoruGuncelle = mFirestore.collection("Menuler").document(konuID);
                    konuSoruGuncelle.update("soru_sayisi", FieldValue.increment(1));
                    konuSoruGuncelle.update("postUnic_autoIncrement", FieldValue.increment(1));

                    //userMap.put("dersID", dersID);

                    DocumentReference dersSoruGuncelle = mFirestore.collection("Menuler").document(dersID);
                    dersSoruGuncelle.update("soru_sayisi", FieldValue.increment(1));

                    Intent loginIntent = new Intent(AddPost.this, MainActivity.class);

                    startActivity(loginIntent);
                    finish();
                }
            }
        });

    }

    public void resimYukle() {
        String user_id = mAuth.getCurrentUser().getUid();
        String uniqueString = UUID.randomUUID().toString();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) +1;

        str = FirebaseStorage.getInstance().getReference()
                .child(String.valueOf(year))
                .child(String.valueOf(month))
                .child(uniqueString+".jpg");

        str.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        newImageUri = uri.toString();
                        fireSore_soruKaydet();
                    }
                });
            }
        });

    }


    public void tanimlama() {
        AcilirListe = findViewById(R.id.spinner_dCevap);
        spinner_ders = findViewById(R.id.spinner_ders);
        spinner_konu = findViewById(R.id.spinner_konu);
        postImage = findViewById(R.id.postImage);
        btnResimSec = findViewById(R.id.btnResimSec);
        btn_soruEkle = findViewById(R.id.btn_soruEkle);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void dersListele() {
        mFirestore.collection("Menuler").whereEqualTo("dersID", "0")
                .orderBy("siralama")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> ders = new ArrayList<>();
                    final List<String> ders_id = new ArrayList<>();

                    ders_id.add("");
                    ders.add("Ders Seçin");

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //ders.add(document.getId());
                        ders_id.add(document.getId());
                        ders.add(document.getString("menu"));
                        //Toast.makeText(getApplicationContext(), document.getId(), Toast.LENGTH_SHORT).show();

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, ders);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_ders.setAdapter(adapter);


                    spinner_ders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //spinner_ders.getSelectedItemPosition();
                            if (spinner_ders.getSelectedItemPosition() != 0) {
                                //String dersasdasd = String.valueOf(spinner_ders.getSelectedItemPosition());
                                dersID = ders_id.get(spinner_ders.getSelectedItemPosition());
                                //String dersasdasd = ders_id.get(2);
                                //Toast.makeText(getApplicationContext(), dersID, Toast.LENGTH_SHORT).show();
                                //dCevap = spinner_ders.getSelectedItem().toString();
                                konuListele(dersID);
                            }else{
                                dersID = ders_id.get(0);
                                konuListele(dersID);

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });
    }


    public void konuListele(String documanID) {
        mFirestore.collection("Menuler").whereEqualTo("dersID", documanID)
                .orderBy("siralama")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> konu = new ArrayList<>();
                    final List<String> konu_id = new ArrayList<>();
                    final List<String> konu_soru_sayisi = new ArrayList<>();
                    final List<String> postUnic_autoIncrement = new ArrayList<>();

                    konu_id.add("");
                    konu.add("Konu Seçin");
                    konu_soru_sayisi.add("");
                    postUnic_autoIncrement.add("");

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        konu.add(document.getString("menu"));
                        konu_id.add(document.getId());
                        //konu_soru_sayisi.add((document.getLong("soru_sayisi")));
                        if(document.get("soru_sayisi") != null){
                            konu_soru_sayisi.add(String.valueOf(document.get("soru_sayisi")));
                        }else{
                            konu_soru_sayisi.add("0");
                        }

                        if(document.get("postUnic_autoIncrement") != null){
                            postUnic_autoIncrement.add(String.valueOf(document.get("postUnic_autoIncrement")));
                        }else{
                            postUnic_autoIncrement.add("0");
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, konu);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_konu.setAdapter(adapter);


                    spinner_konu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (spinner_konu.getSelectedItemPosition() != 0) {
                                konuID = konu_id.get(spinner_konu.getSelectedItemPosition());
                                konu_soru_sayisi_int = Integer.parseInt(konu_soru_sayisi.get(spinner_konu.getSelectedItemPosition()));
                                postUnic_autoIncrement_int = Integer.parseInt(postUnic_autoIncrement.get(spinner_konu.getSelectedItemPosition()));
                                Toast.makeText(getApplicationContext(), konuID, Toast.LENGTH_SHORT).show();
                                konu_Ischeck = true;
                            } else {
                                konu_Ischeck = false;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });
    }


    public void dCevap() {
        AcilirListe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (AcilirListe.getSelectedItemPosition() == 0) {
                    //sonuc.setText(AcilirListe.getSelectedItemPosition());
                    dCevap = "";
                    dCevap_Ischeck = false;
                } else {
                    dCevap_Ischeck = true;
                    dCevap = AcilirListe.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent settingsIntent = new Intent(AddPost.this, MainActivity.class);
        startActivity(settingsIntent);
        finish();
    }

}
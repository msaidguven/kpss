


package com.example.kpss;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button settingsBtn;
    private EditText settingsName;
    private CircleImageView settingsImage;
    private DatabaseReference mDatabase;
    private Uri imageUri = null;
    private Boolean Ischeck = false;
    private ProgressDialog settingsProgress;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);











        mToolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); /*geri tuşu */

        settingsBtn = findViewById(R.id.settings_btn);
        settingsName = findViewById(R.id.settings_name);
        settingsImage = findViewById(R.id.settings_image);
        settingsProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();



        mFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String user_name = task.getResult().getString("name");
                    String user_image = task.getResult().getString("image");

                    //settingsName.setText(Timestamp.now().toString());
                    settingsName.setText(user_name);
                    Picasso.get().load(user_image).into(settingsImage);
                }
            }
        });

        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
            }
        });


        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = settingsName.getText().toString();

                settingsProgress.setTitle("Güncelleniyor");
                settingsProgress.setMessage("Bilgilerinizi güncelliyoruz, lütfen bekleyiniz");
                settingsProgress.show();


                if (!TextUtils.isEmpty(user_name)) {
                    if (Ischeck) {
                        resimYukle();
                    } else {
                        isimGuncelle();
                    }
                }

            }
        });

    }

    private void isimGuncelle() {
        String user_id = mAuth.getCurrentUser().getUid();
        String user_name = settingsName.getText().toString();
        Map userUpdateMap = new HashMap();
        userUpdateMap.put("name", user_name);
        mFirestore.collection("Users").document(user_id).update(userUpdateMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Güncelleme Başarılı ", Toast.LENGTH_LONG).show();
                    settingsProgress.dismiss();
                }
            }
        });
    }

    private void resimGuncelle(String newUri) {
        String user_id = mAuth.getCurrentUser().getUid();
        String user_name = settingsName.getText().toString();
        Map userUpdateMap = new HashMap();
        userUpdateMap.put("name", user_name);
        userUpdateMap.put("image", newUri);
        mFirestore.collection("Users").document(user_id).update(userUpdateMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    //Toast.makeText(getApplicationContext(), "Güncelleme Başarılı ", Toast.LENGTH_LONG).show();
                    //settingsProgress.dismiss();
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
                imageUri = result.getUri();
                settingsImage.setImageURI(imageUri);
                Ischeck = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void resimYukle() {
        String user_id = mAuth.getCurrentUser().getUid();
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images").child(user_id + ".jpg");
        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String newUri = uri.toString();
                        Map userUpdateMap = new HashMap();
                        userUpdateMap.put("image", newUri);
                        isimGuncelle();
                        resimGuncelle(newUri);

                    }
                });
            }
        });
    }




}

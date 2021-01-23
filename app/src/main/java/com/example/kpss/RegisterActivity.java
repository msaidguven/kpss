package com.example.kpss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_userName;
    private EditText register_Email;
    private EditText register_Pass;
    private EditText register_Pass2;
    private Button btn_register;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;

    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_userName = findViewById(R.id.register_userName);
        register_Email = findViewById(R.id.register_Email);
        register_Pass = findViewById(R.id.register_Pass);
        register_Pass2 = findViewById(R.id.register_Pass2);
        btn_register = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = register_userName.getText().toString();
                String email = register_Email.getText().toString();
                String sifre1 = register_Pass.getText().toString();
                String sifre2 = register_Pass2.getText().toString();


                if (!TextUtils.isEmpty(userName) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(sifre1) || !TextUtils.isEmpty(sifre2)) {
                    if (sifre1.equals(sifre2)) {
                        progressDialog.setTitle("Kaydediliyor");
                        progressDialog.setMessage("Hesabınızı oluşturuyoruz");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        register_user(userName, email, sifre1);
                    } else {
                        register_userName.setText("şifreler farklı");
                    }
                }


                Toast.makeText(getApplicationContext(), "mesaj", Toast.LENGTH_SHORT);
            }
        });

    }

    private void register_user(String userName, String email, String sifre1) {
        mAuth.createUserWithEmailAndPassword(email, sifre1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("name", userName);
                    String profil_image_url = "https://firebasestorage.googleapis.com/v0/b/login-4a95d.appspot.com/o/profile_images%2Fuser-01.jpg?alt=media&token=ab95b905-39d3-4c1c-b22e-b0ec824e4f46";
                    userMap.put("image", profil_image_url);
                    userMap.put("time", FieldValue.serverTimestamp());

                    mFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Kayıt Başarılı", Toast.LENGTH_LONG).show();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                            }
                        }
                    });

                    /*
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                            }
                        }
                    });
*/
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Hata " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
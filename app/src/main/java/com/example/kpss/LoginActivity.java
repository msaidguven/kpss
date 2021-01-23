package com.example.kpss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText txtLoginEmail;
    private EditText txtLoginPassword;
    private Button btnLogin;
    private ProgressDialog loginProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        txtLoginEmail = findViewById(R.id.txtLoginEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtLoginEmail.getText().toString();
                String password = txtLoginPassword.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    loginProgress.setTitle("Oturum Açılıyor");
                    loginProgress.setMessage("Hesabınıza giriş yapılıyor. Lütfen bekleyiniz..");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    loginUser(email,password);
                }
            }
        });


    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Giriş Başarılı", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    finish();

                    startActivity(mainIntent);
                }else {
                    loginProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Giriş Yapılamadı "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
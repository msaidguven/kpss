package com.example.kpss;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class UyeSoruAnaliz {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    int toplam;
    private String postID;


    public UyeSoruAnaliz(String docName) {

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("uyeSoruAnaliz").document(docName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    A =  document.get("A") == null ? 0 : Integer.parseInt(task.getResult().get("A").toString()) ;
                    B =  document.get("B") == null ? 0 : Integer.parseInt(task.getResult().get("B").toString()) ;
                    C =  document.get("C") == null ? 0 : Integer.parseInt(task.getResult().get("C").toString()) ;
                    D =  document.get("D") == null ? 0 : Integer.parseInt(task.getResult().get("D").toString()) ;
                    E =  document.get("E") == null ? 0 : Integer.parseInt(task.getResult().get("E").toString()) ;


                    toplam = A + B + C + D + E;
                    if (toplam == 0) {
                        toplam = 1;
                    }

                }
            }
        });

    }


    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public int getD() {
        return D;
    }

    public void setD(int d) {
        D = d;
    }

    public int getE() {
        return E;
    }

    public void setE(int e) {
        E = e;
    }

    public int getToplam() {
        return toplam;
    }

    public void setToplam(int toplam) {
        this.toplam = toplam;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }




}

package com.example.kpss;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class PostModel {
    /*
        private String postLessonID;
        private String postUnitID;
        private String dersName;
        private String konuName;
     */

    private String postID;
    private String postImage;
    private String userID;
    private String dersID;
    private String konuID;
    private String time;
    private int A,B,C,D,E;
    private String d_cevap;
    private String time1;

    private String dersName;
    private String konuName;


    public PostModel(String postID, String postImage) {
        this.postID = postID;
        this.postImage = postImage;
    }

    public PostModel(String postID, String postImage, String time, String time1, String konuID, String d_cevap) {
        this.postID = postID;
        this.postImage = postImage;
        this.time = time;
        this.time1 = time1;
        this.konuID = konuID;
        this.d_cevap = d_cevap;
    }

    public PostModel(String postID, String postImage, String time, String time1, String konuID, String d_cevap, String dersName) {
        this.postID = postID;
        this.postImage = postImage;
        this.time = time;
        this.time1 = time1;
        this.konuID = konuID;
        this.d_cevap = d_cevap;
        this.dersName = dersName;
    }

    public PostModel(String postID, String postImage, String userID, String dersID, String konuID, String time, int a, int b, int c, int d, int e, String d_cevap) {
        this.postID = postID;
        this.postImage = postImage;
        this.userID = userID;
        this.dersID = dersID;
        this.konuID = konuID;
        this.time = time;
        A = a;
        B = b;
        C = c;
        D = d;
        E = e;
        this.d_cevap = d_cevap;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDersID() {
        return dersID;
    }

    public void setDersID(String dersID) {
        this.dersID = dersID;
    }

    public String getKonuID() {
        return konuID;
    }

    public void setKonuID(String konuID) {
        this.konuID = konuID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getD_cevap() {
        return d_cevap;
    }

    public void setD_cevap(String d_cevap) {
        this.d_cevap = d_cevap;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getDersName() {
        return dersName;
    }

    public void setDersName(String dersName) {
        this.dersName = dersName;
    }

    public String getKonuName() {
        return konuName;
    }

    public void setKonuName(String konuName) {
        this.konuName = konuName;
    }
}



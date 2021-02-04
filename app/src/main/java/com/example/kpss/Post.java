package com.example.kpss;

import com.google.firebase.Timestamp;

public class Post {
    private String postID;
    private String postImage;
    private String postLessonID;
    private String postUnitID;
    private String dersName;
    private String konuName;
    private String userID;
    private String dersID;
    private String konuID;
    private String time;
    private String time1;
    private String d_cevap;
    private int A,B,C,D,E;



    public Post() {
    }

    public Post(String postID, String postImage) {
        this.postID = postID;
        this.postImage = postImage;
    }

    public Post(String postID, String postImage, String time) {
        this.postID = postID;
        this.postImage = postImage;
        this.time = time;
    }

    public Post(String postID, String postImage, String time, String time1, String konuID, String d_cevap) {
        this.postID = postID;
        this.postImage = postImage;
        this.time = time;
        this.time1 = time1;
        this.konuID = konuID;
        this.d_cevap = d_cevap;
    }

    public Post(String postID, String postImage, String userID, String dersID, String konuID, String time, int a, int b, int c, int d, int e, String d_cevap) {
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

/*
    public Post(String postID, String postImage, String dersName, String konuName) {
        this.postID = postID;
        this.postImage = postImage;
        this.dersName = dersName;
        this.konuName = konuName;
    }
*/
    public String getPostID() {
        return postID;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostLessonID() {
        return postLessonID;
    }

    public String getPostUnitID() {
        return postUnitID;
    }

    public String getDersName() {
        return dersName;
    }

    public String getKonuName() {
        return konuName;
    }

    public String getUserID() {
        return userID;
    }

    public String getDersID() {
        return dersID;
    }

    public String getKonuID() {
        return konuID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public void setPostLessonID(String postLessonID) {
        this.postLessonID = postLessonID;
    }

    public void setPostUnitID(String postUnitID) {
        this.postUnitID = postUnitID;
    }

    public void setDersName(String dersName) {
        this.dersName = dersName;
    }

    public void setKonuName(String konuName) {
        this.konuName = konuName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDersID(String dersID) {
        this.dersID = dersID;
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

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time;
    }


    public String getD_cevap() {
        return d_cevap;
    }

    public void setD_cevap(String d_cevap) {
        this.d_cevap = d_cevap;
    }
}

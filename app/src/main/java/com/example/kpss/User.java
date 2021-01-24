package com.example.kpss;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private String userID;
    private String name;
    private String image;

    public User(String userID, String name, String image) {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        this.userID = userID;
        this.name = name;
        this.image = image;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

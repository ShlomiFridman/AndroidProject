package com.example.androidproject;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseModule {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private Score max,score;
    public Bitmap img;

    private static FirebaseModule instance;

    private FirebaseModule(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public static FirebaseModule getInstance(){
        if (instance==null)
            instance = new FirebaseModule();
        return instance;
    }

    public FirebaseUser getUser(){
        return this.mAuth.getCurrentUser();
    }

    public FirebaseAuth getAuth(){
        return this.mAuth;
    }
    public FirebaseDatabase getDatabase(){
        return this.database;
    }

    public void setScore(Score score){
        this.score = score;
    }
    public void setMax(Score max){
        this.max = max;
    }
    public Score getScore(){
        return this.score;
    }
    public Score getMax(){
        return this.max;
    }

}

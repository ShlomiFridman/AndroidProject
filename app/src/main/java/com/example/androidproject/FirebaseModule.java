package com.example.androidproject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseModule {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

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

}

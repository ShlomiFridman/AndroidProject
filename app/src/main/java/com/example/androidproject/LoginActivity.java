package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private TextView loginText;
    private EditText email,pass;
    private Button loginBtn,registerBtn;
    private FirebaseModule db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setup();
    }

    private void setup(){
        this.loginText = findViewById(R.id.loginText);
        this.email = findViewById(R.id.loginEmail);
        this.pass = findViewById(R.id.loginPass);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.registerBtn = findViewById(R.id.registerBtn);

        this.loginBtn.setOnClickListener(view->{
            LoginActivity.this.signin(this.email.getText().toString(),this.pass.getText().toString());
        });

        this.registerBtn.setOnClickListener(view->{
            LoginActivity.this.register(this.email.getText().toString(),this.pass.getText().toString());
        });
        db = FirebaseModule.getInstance();
        user = db.getUser();
        if (user!=null) {
            makeToast("Already logged in");
            logged();
        }
        else
            makeToast("No user logged");
        this.loginText.setText(user!=null? "Welcome "+user.getEmail():"Please log in");

    }

    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    public void register(String email,String password){
        db.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            makeToast("Welcome new user "+email);
                            loginText.setText(email+" registered");
                            user = db.getAuth().getCurrentUser();
                            logged();
                        } else {
                            // If sign in fails, display a message to the user.
                            System.err.println(task.getException());
                            makeToast("Failed to create new user");
                        }

                        // ...
                    }
                });
    }

    public void signin(String email,String password){
        db.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            makeToast("Welcome "+email);
                            loginText.setText(email+" logged in");
                            user = db.getAuth().getCurrentUser();
                            logged();
                        } else {
                            // If sign in fails, display a message to the user.
                            System.err.println(task.getException());
                            makeToast("Failed to signed in");
                        }
                    }
                });
    }

    private void logged(){
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

}
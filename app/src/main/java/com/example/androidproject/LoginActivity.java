package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private TextView loginText;
    private EditText email,pass;
    private Button loginBtn,registerBtn;
    private FirebaseModule db;
    private FirebaseUser user;

    private ImageView img;
    private Button imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setup();
    }

    private void setup(){
        this.img = findViewById(R.id.camPicture);
        this.imgBtn = findViewById(R.id.picBtn);
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
            this.email.setText(user.getEmail().toString());
            //logged();
        }
        else
            makeToast("No user logged");

        // take photo section
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(LoginActivity.this,new String[] {Manifest.permission.CAMERA},CAMERA_REQUEST);
                else
                    takePhoto();
            }
        });

        this.loginText.setText(user!=null? "Welcome "+user.getEmail():"Please log in");

    }

    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    public void register(String email,String password){
        if (email.isEmpty() || password.isEmpty()) {
            makeToast("Invalid info");
            return;
        }
        db.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            makeToast("Welcome new user "+email);
                            loginText.setText(email+" registered");
                            user = db.getAuth().getCurrentUser();
                            Score score = new Score(email);
                            FirebaseModule.getInstance().getDatabase().getReference("scores").child(score.getKey()).setValue(score).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    logged();
                                }
                            });
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
        if (email.isEmpty() || password.isEmpty()) {
            makeToast("Invalid info");
            return;
        }
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

    // take photo section

    final int CAMERA_ACTION = 101;
    final int CAMERA_REQUEST = 201;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_ACTION && resultCode==RESULT_OK) {
            img.setImageBitmap((Bitmap) data.getExtras().get("data"));
            this.db.img = (Bitmap) data.getExtras().get("data");
        }
    }

    public void takePhoto(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, CAMERA_ACTION);
        else
            makeToast("Camera access denied");
    }
}
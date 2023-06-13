package com.example.evermore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class evermore_login extends AppCompatActivity {
    Intent intent;
    Button button_signup, btnLogin;
    EditText username, pass;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(evermore_login.this, Home.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evermore_login);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        button_signup = findViewById(R.id.btn_signup);
        button_signup.setOnClickListener(view -> {
            intent = new Intent(this, evermore_signup.class);
            startActivity(intent);
        });

        username = findViewById(R.id.editTextUsername);
        pass = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(view -> {
            String email = username.getText().toString();
            String password = pass.getText().toString();

            try {
                if (email.isEmpty() || password.isEmpty()) throw new IllegalArgumentException("Email or password cannot be empty.");
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(evermore_login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(evermore_login.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(evermore_login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (IllegalArgumentException e) {
                Toast.makeText(evermore_login.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
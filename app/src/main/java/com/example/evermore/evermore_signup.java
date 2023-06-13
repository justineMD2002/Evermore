package com.example.evermore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class evermore_signup extends AppCompatActivity {


    Button btnSignup;
    EditText first, last, dob, username, pass;
    String course;
    Spinner spinner;
    Intent intent;
    FirebaseAuth mAuth;
    ArrayAdapter<CharSequence> adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evermore_signup);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        btnSignup = findViewById(R.id.btn_createAccount);
        spinner = (Spinner) findViewById(R.id.spinner);
        course = "None";

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_data, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                course = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mAuth = FirebaseAuth.getInstance();
        first = findViewById(R.id.editTextFirstname);
        last = findViewById(R.id.editTextLastname);
        dob = findViewById(R.id.editTextDOB);
        username = findViewById(R.id.editTextUsername);
        pass = findViewById(R.id.editTextPassword);

        btnSignup.setOnClickListener(view -> {
            String email = username.getText().toString();
            String password = pass.getText().toString();
            String firstname = first.getText().toString();
            String lastname = last.getText().toString();
            String birthdate = dob.getText().toString();
            try {
                if (email.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || birthdate.isEmpty()) {
                    throw new IllegalArgumentException("Email or password cannot be empty.");
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User has been created in Firebase Authentication, now register in Firestore
                                    try {
                                        registerUser(firstname, lastname, birthdate, course);
                                    }catch (Exception e) {
                                        Toast.makeText(evermore_signup.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                                        first.setText("");
                                        last.setText("");
                                        dob.setText("");
                                        username.setText("");
                                        pass.setText("");
                                    }
                                    Toast.makeText(evermore_signup.this, "Account created.", Toast.LENGTH_SHORT).show();
                                } else {
                                    username.setText("");
                                    pass.setText("");
                                    first.setText("");
                                    last.setText("");
                                    dob.setText("");
                                    Toast.makeText(evermore_signup.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (IllegalArgumentException e) {
                Toast.makeText(evermore_signup.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void registerUser(String first, String last, String dob, String course) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("firstname", first);
        user.put("lastname", last);
        user.put("dob", dob);
        user.put("course", course);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        intent = new Intent(evermore_signup.this, evermore_login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(evermore_signup.this, "Account created!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(evermore_signup.this, "Error creating user.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
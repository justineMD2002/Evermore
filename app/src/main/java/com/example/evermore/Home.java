package com.example.evermore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;

public class Home extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Intent intent;
    Dialog dialog, dialog2;
    CardView item1, item2, item3;
    TextView selectedCourse, selectedCourse2;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.unlock_dialog);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        }
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2 = new Dialog(Home.this);
        dialog2.setContentView(R.layout.confirm_lock_dialog);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        }
        dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        drawerLayout = findViewById(R.id.drawer_layout);
        item1 = findViewById(R.id.medsurg);
        item2 = findViewById(R.id.informatics);
        item3 = findViewById(R.id.patho);
        selectedCourse= dialog.findViewById(R.id.tfCourseSelected);
        selectedCourse2= dialog2.findViewById(R.id.tfCourseSelected);

        item1.setOnClickListener(view -> {
            selectedCourse.setText("Medical-Surgical Nursing");
            if(getBooleanFromPreference("Medical-Surgical Nursing")) { // Pass the course to getBooleanFromPreference
                intent = new Intent(Home.this, Flashcards.class);
                intent.putExtra("course", "Medical-Surgical Nursing");
                startActivity(intent);
            } else {
                confirm("Medical-Surgical Nursing");
            }
        });
        item1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedCourse2.setText("Medical-Surgical Nursing");
                confirmLock("Medical-Surgical Nursing");
                return true;
            }
        });
        item2.setOnClickListener(view -> {
            selectedCourse.setText("Nursing Informatics");
            if(getBooleanFromPreference("Nursing Informatics")) { // Pass the course to getBooleanFromPreference
                intent = new Intent(Home.this, Flashcards.class);
                intent.putExtra("course", "Nursing Informatics");
                startActivity(intent);
            } else {
                confirm("Nursing Informatics");
            }
        });
        item2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedCourse2.setText("Nursing Informatics");
                confirmLock("Nursing Informatics");
                return true;
            }
        });
        item3.setOnClickListener(view -> {
            selectedCourse.setText("Pathophysiology");
            if(getBooleanFromPreference("Pathophysiology")) { // Pass the course to getBooleanFromPreference
                intent = new Intent(Home.this, Flashcards.class);
                intent.putExtra("course", "Pathophysiology");
                startActivity(intent);
            } else {
                confirm("Pathophysiology");
            }
        });
        item3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedCourse2.setText("Pathophysiology");
                confirmLock("Pathophysiology");
                return true;
            }
        });


        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.facebook.com/radarada.dadan?mibextid=LQQJ4d"));
                        startActivity(intent);
                        break;
                    case R.id.calc:
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(Home.this, evermore_login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Home.this, "Logged out", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.email:
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"daugdaug.justine.m@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Evermore Inquiry");
                        try {
                            startActivity(Intent.createChooser(intent, "Send Email"));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(Home.this, "No email clients found.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.phone:
                        intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + "09150976481"));
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });


    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
    protected void confirm(String course) {
        dialog.show();
        Button yes = dialog.findViewById(R.id.btnYes);
        Button no = dialog.findViewById(R.id.btnNo);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Home.this, Flashcards.class);
                intent.putExtra("course", course);
                saveBooleanInPreference(course); // Pass the course to saveBooleanInPreference
                startActivity(intent);
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    protected void confirmLock(String course) {
        dialog2.show();
        Button yes = dialog2.findViewById(R.id.btnYes);
        Button no = dialog2.findViewById(R.id.btnNo);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(Home.this, "Course Locked", Toast.LENGTH_SHORT).show();
                dialog2.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();
                dialog2.dismiss();
            }
        });
    }

    public void saveBooleanInPreference(String course) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(course, true); // Use the course as the key
        editor.apply();
    }

    public boolean getBooleanFromPreference(String course) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(course, false); // Use the course as the key
    }
}
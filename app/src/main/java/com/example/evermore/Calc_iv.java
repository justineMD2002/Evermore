package com.example.evermore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Calc_iv extends AppCompatActivity {
    TextView rate, time;
    EditText vol, hrs;
    Button calc, clear;
    @SuppressLint({"MissingInflatedId", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_iv);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        vol = findViewById(R.id.editTextVol);
        hrs = findViewById(R.id.editTextHr);
        rate = findViewById(R.id.tfRate);
        time = findViewById(R.id.tfTime);
        calc = findViewById(R.id.btnCalc);
        clear = findViewById(R.id.btnClear);

        calc.setOnClickListener(view -> {
            double r, ml, hr, tm;
            try {
                ml = Double.parseDouble(vol.getText().toString());
                hr = Double.parseDouble(hrs.getText().toString());
                if(hr == 0) throw new ArithmeticException();
                r = ml / hr;
                tm = ml / r;
                rate.setText("Infusion rate: " + String.format("%.2f", r));
                time.setText("Infusion time: " + String.format("%.2f", tm));
            } catch(Exception a) {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                vol.setText("");
                hrs.setText("");
                rate.setText("");
                time.setText("");
            }
        });

        clear.setOnClickListener(view -> {
            vol.setText("");
            hrs.setText("");
            rate.setText("");
            time.setText("");
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
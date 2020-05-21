package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView firstText, secondText;
    private Button adminButton, userButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adminButton = findViewById(R.id.adminButton);
        userButton = findViewById(R.id.userButton);

        firstText = findViewById(R.id.firstText);
        firstText.animate().alphaBy(1).setDuration(2000);
        secondText = findViewById(R.id.secondText);
        secondText.animate().alphaBy(1).setDuration(2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //access the token

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                finish();
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class));
                finish();
            }
        });
    }
}

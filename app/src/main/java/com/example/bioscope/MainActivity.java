package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView backgroundImg, backgroundImg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundImg = findViewById(R.id.backGroundImg);
        backgroundImg2 = findViewById(R.id.backGroundImg2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //access the token
        Glide.with(this).asGif().load(getResources().getString(R.string.gif)).into(backgroundImg);
        Glide.with(this).asGif().load(getResources().getString(R.string.gif2)).into(backgroundImg2);
    }

    public void watchFunction(View view) {
        startActivity(new Intent(getApplicationContext(), UserLoginActivity.class));
    }

    public void adminFunction(View view) {
        startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
        finish();

    }
}

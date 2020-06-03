package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.OTPojo;
import com.example.bioscope.POJO.UserSignUp;
import com.example.bioscope.Utility.Config;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPCheckerActivity extends AppCompatActivity {

    private EditText et1, et2, et3, et4, et5, et6;
    private TextView timerText;
    private Button verifyButton;
    private ConstraintLayout linearLayout;
    private ImageView resendOTP;

    private String email, password, username;
    private String otpValue;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_input_layout);

        et1 = findViewById(R.id.inp_1);
        et2 = findViewById(R.id.inp_2);
        et3 = findViewById(R.id.inp_3);
        et4 = findViewById(R.id.inp_4);
        et5 = findViewById(R.id.inp_5);
        et6 = findViewById(R.id.inp_6);

        timerText = findViewById(R.id.timerText);
        resendOTP = findViewById(R.id.resendOTP);
        verifyButton = findViewById(R.id.confirmOTP);
        verifyButton.setEnabled(false);
        linearLayout = findViewById(R.id.timerHousingLayout);

        email = getIntent().getStringExtra("_email_");
        password = getIntent().getStringExtra("_password_");
        username = getIntent().getStringExtra("_username_");

        sendOTP(email);
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences("MY_PREFS",MODE_PRIVATE);

        startTimer();
        manageEditTexts();

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                sendOTP(email);
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredText = et1.getText().toString() + et2.getText().toString()+ et3.getText().toString() + et4.getText().toString()+ et5.getText().toString()+ et6.getText().toString();
                if(enteredText.equals(otpValue)){
                    createUser();
                }else{
                    Snackbar.make(linearLayout, "Incorrect OTP.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(){
        progressDialog = ProgressDialog.show(this, "", "Signing up. Please wait...", true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes user = retrofit.create(UserRoutes.class);
        UserSignUp userSignUp = new UserSignUp(username, email, password);
        Call<UserSignUp> call = user.createUser(userSignUp);
        call.enqueue(new Callback<UserSignUp>() {
            @Override
            public void onResponse(Call<UserSignUp> call, Response<UserSignUp> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_TOKEN", response.body().getToken());
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainUserActivity.class).putExtra("callsc", 1));
                    finish();
                }else{
                    Snackbar.make(linearLayout, getResources().getString(R.string.email_exists_message), Snackbar.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                countDownTimer.cancel();
            }

            @Override
            public void onFailure(Call<UserSignUp> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                progressDialog.dismiss();
                Snackbar.make(linearLayout, "Something went wrong. Try again", Snackbar.LENGTH_SHORT).show();
                countDownTimer.cancel();
            }
        });

    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int)millisUntilFinished/(1000);
                timerText.setText("00:"+String.valueOf(seconds));
            }
            public void onFinish() {
            }
        }.start();
    }

    private void sendOTP(String email){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes admin = retrofit.create(UserRoutes.class);
        OTPojo otPojo = new OTPojo(email);
        Call<OTPojo> call = admin.sendOTP(otPojo);
        call.enqueue(new Callback<OTPojo>() {
            @Override
            public void onResponse(Call<OTPojo> call, Response<OTPojo> response) {
                if(response.isSuccessful()){
                    Snackbar.make(linearLayout, "OTP has been sent!", Snackbar.LENGTH_SHORT).show();
                    otpValue = String.valueOf(response.body().getConfirmationCode());
                    verifyButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<OTPojo> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                Snackbar.make(linearLayout, "Something went wrong. Try again", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private void manageEditTexts(){
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=1){
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=1){
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=1){
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=1){
                    et5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=1){
                    et6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=1){
                    verifyButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
        finish();
    }
}
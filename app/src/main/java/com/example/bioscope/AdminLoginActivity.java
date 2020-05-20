package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bioscope.API.AdminRoutes;
import com.example.bioscope.POJO.CreateAdmin;
import com.example.bioscope.POJO.LoginAdminPOJO;
import com.example.bioscope.Utility.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLoginActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private FloatingActionButton loginFab, signUpFab;
    private TextView loginTV, signUpTV;
    private TextInputEditText emailETAdminLogin, passwordETAdminLogin, spclTokenETAdminLogin;
    private TextInputEditText emailETAdminSignUp, passwordETAdminSingUp, spclTokenAdminSignUp, usernameETAdminSignUp;

    private ProgressBar signUpPogressBar;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        constraintLayout = findViewById(R.id.housingLayout);
        slidingUpPanelLayout = findViewById(R.id.adminPanelLayout);
        loginFab = findViewById(R.id.fabLoginAdmin);
        signUpFab = findViewById(R.id.fabSignUpAdmin);
        loginTV = findViewById(R.id.adminLoginText);
        signUpTV = findViewById(R.id.adminsignupText);
        loginFab = findViewById(R.id.fabLoginAdmin);

        emailETAdminSignUp = findViewById(R.id.emailETAdminSignup);
        passwordETAdminSingUp = findViewById(R.id.passwordETAdminSignup);
        spclTokenAdminSignUp = findViewById(R.id.specTokenAdminSignupET);
        usernameETAdminSignUp = findViewById(R.id.usernameETAdminSignup);

        emailETAdminLogin = findViewById(R.id.emailETadminLogin);
        passwordETAdminLogin = findViewById(R.id.passwordETAdminLogin);
        spclTokenETAdminLogin = findViewById(R.id.specTokenAdminLoginET);

        signUpPogressBar = findViewById(R.id.adminSignupPB);
    }


    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        ///if the token is available then  login user directly else show login/signup screen
        if(sharedPreferences.getString("ADMIN_TOKEN", null)!=null){
            startActivity(new Intent(this, UploaderActivity.class));
            finish();
            return;
        }

        setPanelAttributes();
        setClickListeners();
    }

    private void setPanelAttributes(){
        slidingUpPanelLayout.setPanelHeight(0);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void setClickListeners(){
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        loginFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailETAdminLogin.getText().toString();
                String password = passwordETAdminLogin.getText().toString();
                String specialToken = spclTokenETAdminLogin.getText().toString();
                if(validateLoginInput(email, password, specialToken)){
                    loginUser(email, password, specialToken);
                }
                return;
            }
        });

        signUpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameETAdminSignUp.getText().toString(),
                        email = emailETAdminSignUp.getText().toString(),
                        password = passwordETAdminSingUp.getText().toString(),
                        specialToken = spclTokenAdminSignUp.getText().toString();

                if(validateInput(username, password, email, specialToken)){
                    createAdmin(username, password, email, specialToken);
                }
                return;
            }
        });
    }


    private boolean validateInput(String username, String password, String email, String specialToken) {
        if(username.equals("") || email.equals("") || password.equals("") || specialToken.equals("")){
            Snackbar.make(constraintLayout, "Cannot leave fields empty.", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(password.length()<8){
            Snackbar.make(constraintLayout, "Password should be more than 8 characters", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(specialToken.length()<15){
            Snackbar.make(constraintLayout, "Chhabbi kahan hai?", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateLoginInput(String email, String password, String specialToken){
        if(email.isEmpty() || password.isEmpty() || specialToken.isEmpty()){
           Snackbar.make(constraintLayout, "Cannot leave fields empty!", Snackbar.LENGTH_SHORT).show();
           return false;
        }else if(password.length() < 8 || specialToken.length() <15){
            Snackbar.make(constraintLayout, "Length not sufficient!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearSignUpInput(){
        emailETAdminSignUp.setText(null);
        usernameETAdminSignUp.setText(null);
        spclTokenAdminSignUp.setText(null);
        passwordETAdminSingUp.setText(null);
    }

    private void clearLoginInput(){
        emailETAdminLogin.setText(null);
        passwordETAdminLogin.setText(null);
        spclTokenETAdminLogin.setText(null);
    }

    private void createAdmin(String username, String password, String email, String specialToken) {
        signUpPogressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        CreateAdmin createUser = new CreateAdmin(username, password, email);
        Call<CreateAdmin> call = admin.createAdmin(createUser, specialToken);
        call.enqueue(new Callback<CreateAdmin>() {
            @Override
            public void onResponse(Call<CreateAdmin> call, Response<CreateAdmin> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ADMIN_TOKEN", response.body().getToken());
                    editor.apply();
                    clearSignUpInput();
                    startActivity(new Intent(getApplicationContext(), UploaderActivity.class));
                    signUpPogressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CreateAdmin> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                Snackbar.make(constraintLayout, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                signUpPogressBar.setVisibility(View.GONE);
                clearSignUpInput();
            }
        });
    }

    private boolean loginUser(String email, String password, String specialToken){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();

        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        LoginAdminPOJO loginAdmin = new LoginAdminPOJO(email, password);
        Call<LoginAdminPOJO> call = admin.loginAdmin(loginAdmin, specialToken);
        call.enqueue(new Callback<LoginAdminPOJO>() {
            @Override
            public void onResponse(Call<LoginAdminPOJO> call, Response<LoginAdminPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ADMIN_TOKEN", response.body().getToken());
                    editor.apply();
                    clearLoginInput();
                    startActivity(new Intent(getApplicationContext(), UploaderActivity.class));
                }
            }

            @Override
            public void onFailure(Call<LoginAdminPOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                Snackbar.make(constraintLayout, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                clearLoginInput();
            }
        });

        return false;
    }
}

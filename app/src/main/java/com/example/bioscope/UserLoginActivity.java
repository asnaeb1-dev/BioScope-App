package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bioscope.API.AdminRoutes;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.CreateAdmin;
import com.example.bioscope.POJO.UserSignUp;
import com.example.bioscope.Utility.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLoginActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private TextView loginInstruct, signUpInstruct;
    private TextInputEditText userNameET, emailET, passwordET, emailETLogin, passwordETLogin;
    private FloatingActionButton signupFAB, loginFAB;
    private ProgressBar signUPPB, loginPB;
    private ConstraintLayout houserLayoutUser;
    private SharedPreferences sharedPreferences;
    private ImageView userLoginBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if(getSharedPreferences("MY_PREFS",MODE_PRIVATE).getString("USER_TOKEN", null)!=null){
            startActivity(new Intent(this, MainUserActivity.class));
            finish();
        }

        userLoginBack = findViewById(R.id.userLoginBack);
        houserLayoutUser = findViewById(R.id.house_Layout_user);
        slidingUpPanelLayout = findViewById(R.id.userPanel);
        //sign up
        loginInstruct = findViewById(R.id.loginInstructTV);
        signUpInstruct = findViewById(R.id.signUpInstructTV);
        userNameET = findViewById(R.id.usernameETUserSignUp);
        emailET = findViewById(R.id.emailETUserSignUp);
        passwordET = findViewById(R.id.passwordETUserSignUp);
        signupFAB = findViewById(R.id.userSignUpFAB);
        signUPPB = findViewById(R.id.userSignUpPB);

        //log in
        emailETLogin = findViewById(R.id.emailETUserLogin);
        passwordETLogin = findViewById(R.id.passswordETUserLogin);
        loginFAB = findViewById(R.id.userLoginFAB);
        loginPB = findViewById(R.id.userLoginPB);

    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        Glide.with(this).asGif().load(getResources().getString(R.string.gif)).into(userLoginBack);
        signupFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameET.getText().toString(),
                        email = emailET.getText().toString(),
                        password = passwordET.getText().toString();

                signUPPB.setVisibility(View.VISIBLE);
                if(validateSignUpInput(username, email, password)){
                    createUser(username, email, password);
                }else{
                    signUPPB.setVisibility(View.GONE);
                }
                return;
            }
        });

        loginFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailETLogin.getText().toString(),
                        password = passwordETLogin.getText().toString();

                loginPB.setVisibility(View.VISIBLE);
                if(validateLoginInput(email, password)){
                    loginUser(email, password);
                }else {
                    loginPB.setVisibility(View.GONE);
                }
            }
        });

        signUpInstruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        passwordETLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<8){
                    passwordETLogin.setError(getResources().getString(R.string.password_size_prompt));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 8){
                    passwordET.setError(getResources().getString(R.string.password_size_prompt));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loginUser(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes admin = retrofit.create(UserRoutes.class);
        UserSignUp userSignUp = new UserSignUp(email, password);
        Call<UserSignUp> call = admin.loginUser(userSignUp);
        call.enqueue(new Callback<UserSignUp>() {
            @Override
            public void onResponse(Call<UserSignUp> call, Response<UserSignUp> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_TOKEN", response.body().getToken());
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainUserActivity.class));
                    finish();
                }else{
                    Snackbar.make(houserLayoutUser, "Incorrect email or password.", Snackbar.LENGTH_SHORT).show();
                }
                signUPPB.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserSignUp> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                signUPPB.setVisibility(View.GONE);
                Snackbar.make(houserLayoutUser, "Something went wrong. Try again", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateLoginInput(String email, String password){
        if(email.isEmpty() || password.isEmpty()){
            Snackbar.make(houserLayoutUser, getResources().getString(R.string.fields_empty_prompt), Snackbar.LENGTH_SHORT).show();
            return false;
        }else if( password.length()< 8){
            Snackbar.make(houserLayoutUser, getResources().getString(R.string.password_size_prompt), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateSignUpInput(String username, String email, String password){
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Snackbar.make(houserLayoutUser, getResources().getString(R.string.fields_empty_prompt), Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(password.length() < 8){
            Snackbar.make(houserLayoutUser, getResources().getString(R.string.password_size_prompt), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createUser(String username, String email, String password){
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
                    startActivity(new Intent(getApplicationContext(), MainUserActivity.class));
                    finish();
                }else{
                    Snackbar.make(houserLayoutUser, getResources().getString(R.string.email_exists_message), Snackbar.LENGTH_SHORT).show();
                }
                signUPPB.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserSignUp> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                signUPPB.setVisibility(View.GONE);
                Snackbar.make(houserLayoutUser, "Something went wrong. Try again", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            super.onBackPressed();
        }else{
            finish();
        }
    }
}

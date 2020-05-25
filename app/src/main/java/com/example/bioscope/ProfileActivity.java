package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bioscope.API.AdminRoutes;
import com.example.bioscope.POJO.LogoutPOJO;
import com.example.bioscope.Utility.Config;
import com.google.android.material.appbar.AppBarLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageView settingButton, backButton;

    private LinearLayout genreList;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        slidingUpPanelLayout = findViewById(R.id.profileSlidingPanel);
        circleImageView = findViewById(R.id.profilePicture);
        settingButton = findViewById(R.id.settingsProfile);
        backButton = findViewById(R.id.backButtonProfile);
        recyclerView = findViewById(R.id.moviesWatchedRV);
        genreList = findViewById(R.id.genreListProfile);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        slidingUpPanelLayout.setPanelHeight(height/2 + 100);
    }

    private void getProfile(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();
        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        String token = getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null);
        Call<LogoutPOJO> call = admin.logoutAdmin(token);
        call.enqueue(new Callback<LogoutPOJO>() {
            @Override
            public void onResponse(Call<LogoutPOJO> call, Response<LogoutPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;

                }
            }

            @Override
            public void onFailure(Call<LogoutPOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

    }
}

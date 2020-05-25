package com.example.bioscope;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.LogoutPOJO;
import com.example.bioscope.Utility.Config;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SearchView searchView;
    private LinearLayout watchNowLL, topRatedLL;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        searchView = findViewById(R.id.searchView);
        watchNowLL = findViewById(R.id.watchNowLL);
        topRatedLL = findViewById(R.id.topRatedLL);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        progressDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        progressDialog.show();
        getWatchNow();
        getTopRated(7);
    }

    private void getTopRated(int i) {
        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        Call<List<CinemaPOJO>> call = user.getMoviesByRating(i, getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<CinemaPOJO>>() {
            @Override
            public void onResponse(Call<List<CinemaPOJO>> call, Response<List<CinemaPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<CinemaPOJO> list = response.body();
                    topRatedLL.removeAllViews();
                    for(CinemaPOJO cinema : list){
                        if(cinema.getPosters().get(0).getPosterPath()!=null){
                            populateRatingList(cinema.getPosters().get(0).getPosterPath(), cinema.getId());
                        }
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<CinemaPOJO>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ERROR", t.getMessage());
            }
        });

    }

    private void populateRatingList(String posterPath, final String id) {
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setPadding(10, 10, 10, 10);
        Glide.with(this)
                .asBitmap()
                .load(new Config().getImageBaseUrl() + posterPath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),resource);
                        dr.setCornerRadius(20);
                        imageView.setImageDrawable(dr);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        topRatedLL.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra("movie_id", id);
                startActivity(intent);
            }
        });
    }

    private Retrofit initializeRetrofit(String url){
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void getWatchNow(){
        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        Call<List<CinemaPOJO>> call = user.getMovies(getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<CinemaPOJO>>() {
            @Override
            public void onResponse(Call<List<CinemaPOJO>> call, Response<List<CinemaPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<CinemaPOJO> list = response.body();
                    watchNowLL.removeAllViews();
                    for(CinemaPOJO cinema : list){
                        if(cinema.getPosters().get(0).getPosterPath()!=null){
                            populateWatchNow(cinema.getPosters().get(0).getPosterPath(), cinema.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CinemaPOJO>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void populateWatchNow(String path, final String id){
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setPadding(10, 10, 10, 10);
        Glide.with(this)
                .asBitmap()
                .load(new Config().getImageBaseUrl() + path)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),resource);
                        dr.setCornerRadius(20);
                        imageView.setImageDrawable(dr);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        watchNowLL.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra("movie_id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_user, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;

            case R.id.nav_settings:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.now_Playing:
                Toast.makeText(this, "Slideshow", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(MainUserActivity.this)
                        .setTitle("Logout user")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = ProgressDialog.show(getApplicationContext(), "", "Loading. Please wait...", true);
                                progressDialog.show();
                                logoutUser();
                            }
                        }).setNegativeButton("No", null).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes user = retrofit.create(UserRoutes.class);
        Call<LogoutPOJO> call = user.logoutUser(getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<LogoutPOJO>() {
            @Override
            public void onResponse(Call<LogoutPOJO> call, Response<LogoutPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    if(response.body().getMessage().equals("logged_out")){
                        SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor sf = sharedPreferences.edit();
                        sf.remove("USER_TOKEN");
                        sf.apply();
                        progressDialog.dismiss();
                        startActivity(new Intent(MainUserActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutPOJO> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ERROR", t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}

package com.example.bioscope;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.DialogBox.GenreDialog;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.LogoutPOJO;
import com.example.bioscope.Utility.Config;
import com.example.bioscope.Utility.GenreSelectorRV;
import com.example.bioscope.Utility.LastPlayed;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView refreshButton;
    private LinearLayout superHeroLL, mainUserLL, actionLL, comedyLL, horrorLL, hollywoodLL, bollywoodLL, crimeLL, romanceLL;
    private ProgressDialog progressDialog;
    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        refreshButton = findViewById(R.id.refresh);

        actionLL = findViewById(R.id.actionMoviesLL);
        superHeroLL = findViewById(R.id.superheroLL);
        comedyLL = findViewById(R.id.comedyMoviesLL);
        horrorLL = findViewById(R.id.horrorMoviesLL);
        crimeLL = findViewById(R.id.crimeMoviesLL);
        hollywoodLL = findViewById(R.id.hollywoodMoviesLL);
        bollywoodLL = findViewById(R.id.bollywoodMoviesLL);
        romanceLL = findViewById(R.id.romanceMoviesLL);

        mainUserLL = findViewById(R.id.mainUserLL);
        navigationView.setNavigationItemSelectedListener(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        if (getIntent().getIntExtra("callsc", 0) == 1) {
            GenreDialog genreDialog = new GenreDialog(this);
            genreDialog.generateGenreDialog(width, height);
        }

        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), "Loading. Please wait...", true);
        progressDialog.show();

        getMoviesByCategory("superhero");
        getMoviesByCategory("action");
        getMoviesByCategory("comedy");
        getMoviesByCategory("horror");
        getMoviesByCategory("crime");
        getMoviesByCategory("romance");
        getMoviesByIndustry("hollywood");
        getMoviesByIndustry("bollywood");
    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshButton.animate().rotation(360f).setDuration(1000);
                progressDialog = ProgressDialog.show(MainUserActivity.this, getResources().getString(R.string.app_name), "Loading. Please wait...", true);
                progressDialog.show();

                getMoviesByCategory("superhero");
                getMoviesByCategory("action");
                getMoviesByCategory("comedy");
                getMoviesByCategory("horror");
                getMoviesByCategory("crime");
                getMoviesByCategory("romance");
                getMoviesByIndustry("hollywood");
                getMoviesByIndustry("bollywood");

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView movieName = headerView.findViewById(R.id.nav_view_title);
        CircleImageView circleImageView = headerView.findViewById(R.id.nav_view_poster);
        ImageView backdrop = headerView.findViewById(R.id.posterBackground);

        final LastPlayed lastPlayed = new LastPlayed(this);
        if(lastPlayed.getVideoName()!=null){
            movieName.setText(lastPlayed.getVideoName());
            Glide.with(this).load(new Config().getImageBaseUrl() + lastPlayed.getPoster()).into(circleImageView);
            Glide.with(this).load(new Config().getImageBaseUrl() + lastPlayed.getBackdrop()).into(backdrop);

        }else{
            movieName.setText("BioScope");
        }
        backdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra("movie_id", lastPlayed.getVideoID());
                startActivity(intent);
            }
        });

    }

    private void getMoviesByIndustry(final String industry) {
        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        Call<List<CinemaPOJO>> call = user.getMoviesByIndustry(industry ,getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<CinemaPOJO>>() {
            @Override
            public void onResponse(Call<List<CinemaPOJO>> call, Response<List<CinemaPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<CinemaPOJO> list = response.body();

                    switch (industry){

                        case "hollywood":
                            populateList(hollywoodLL, list, "industry", "hollywood");
                            break;

                        case "bollywood":
                            populateList(bollywoodLL, list, "industry", "bollywood");
                            break;

                        default:
                            break;
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

    private Retrofit initializeRetrofit(String url){
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void getMoviesByCategory(final String category){
        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        Call<List<CinemaPOJO>> call = user.getMoviesByCategory(category ,getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<CinemaPOJO>>() {
            @Override
            public void onResponse(Call<List<CinemaPOJO>> call, Response<List<CinemaPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<CinemaPOJO> list = response.body();

                    switch (category){
                        case "superhero":
                            populateList(superHeroLL, list, "category", category);
                            break;

                        case "action":
                            populateList(actionLL, list, "category", category);
                            break;

                        case "horror":
                            populateList(horrorLL, list, "category", category);
                            break;

                        case "comedy":
                            populateList(comedyLL, list, "category", category);
                            break;

                        case "crime":
                            populateList(crimeLL, list, "category", category);
                            break;

                        case "romance":
                            populateList(romanceLL, list, "category", category);
                            break;

                        default:
                            break;
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

    private void populateList(LinearLayout linearLayout, final List<CinemaPOJO> movieList, final String value, final String sender){
        linearLayout.removeAllViews();
        for(final CinemaPOJO cinema : movieList){
            final ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding(10, 10, 10, 10);

            if(cinema.getPosters().get(0).getPosterPath()!=null){
                Glide.with(this)
                        .asBitmap()
                        .load(new Config().getImageBaseUrl() + cinema.getPosters().get(0).getPosterPath())
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
            }else{
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher_bioscope_round));
            }
            linearLayout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                    intent.putExtra("movie_id", cinema.getId());
                    startActivity(intent);
                }
            });
        }

        View view = this.getLayoutInflater().inflate(R.layout.next_button, null);
        CardView cardView = view.findViewById(R.id.clicker_card);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, MovieListerActivity.class);
                if(value.equals("category")){
                    intent.putExtra("callSc", 0);
                    intent.putExtra("_title_", sender);
                }else{
                    intent.putExtra("callSc", 1);
                    intent.putExtra("_title_", sender);
                }
                startActivity(intent);
            }
        });

        linearLayout.addView(view);
        progressDialog.dismiss();
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
                startActivity(new Intent(this, SettingActivity.class));
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
                                progressDialog = ProgressDialog.show(MainUserActivity.this, "", "Loading. Please wait...", true);
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

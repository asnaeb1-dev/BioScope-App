package com.example.bioscope;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bioscope.API.TMDBRoutes;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.LogoutUser;
import com.example.bioscope.POJO.MoviePOJO;
import com.example.bioscope.POJO.TMDBAPI.NowPlaying;
import com.example.bioscope.Utility.Config;
import com.example.bioscope.Utility.MainMovieObject;
import com.example.bioscope.Utility.TMDBMovieObject;
import com.example.bioscope.Utility.TmdbRVAdapter;
import com.example.bioscope.Utility.ViewPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ProgressBar mainScreenPB;
    private ViewPager carousalVP;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout horizontalScrollView;
    private AppBarLayout appBarLayout;
    private ImageView backgroundImage;

    private RecyclerView contentRV;

    private ArrayList<MainMovieObject> movieList;
    private ArrayList<TMDBMovieObject> nowPlayingList;
    private Toolbar toolbar;
    private int width = -1, height = -1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        toolbar = findViewById(R.id.toolbar);
        contentRV = findViewById(R.id.contentRV);
        mainScreenPB = findViewById(R.id.mainScreenPB);
        backgroundImage = findViewById(R.id.background_image);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        setSupportActionBar(toolbar);
        appBarLayout = findViewById(R.id.mainAppBar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToobarLayout);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        carousalVP = findViewById(R.id.infiniteVP);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        movieList = new ArrayList<>();
        nowPlayingList = new ArrayList<>();
        getAllMovies();
        getNowPlaying();
        getAllMoviesRating(7);
        carousalVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                collapsingToolbarLayout.setTitle(movieList.get(position).getTitle());
                Glide.with(MainUserActivity.this)
                        .asBitmap()
                        .load(new Config().getImageBaseUrl()+movieList.get(position).getBackdropPath())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Blurry.with(MainUserActivity.this).from(resource).into(backgroundImage);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void activateToolBar(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    collapsingToolbarLayout.setTitle("Now Playing");
                }
                else {
                    //Expanded
                    if(movieList.size()!=0) {
                        collapsingToolbarLayout.setTitle(movieList.get(carousalVP.getCurrentItem()).getTitle());
                    }else{
                        collapsingToolbarLayout.setTitle("Now Playing");
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_user, menu);
        return true;
    }
    private void getNowPlaying(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getTmdbBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBRoutes tmdb = retrofit.create(TMDBRoutes.class);
        Call<NowPlaying> call = tmdb.getNowPlaying(Config.getTMDBAPIKEY());
        call.enqueue(new Callback<NowPlaying>() {
            @Override
            public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    NowPlaying movie = response.body();
                    for(int i = 0;i< movie.getResults().size();i++){
                        nowPlayingList.add(new TMDBMovieObject(movie.getResults().get(i).getTitle(), movie.getResults().get(i).getId(), movie.getResults().get(i).getPosterPath(), movie.getResults().get(i).getBackdropPath()));
                    }
                    Log.i("TAG", String.valueOf(movie.getResults().size()));
                    contentRV.setAdapter(new TmdbRVAdapter(MainUserActivity.this, nowPlayingList, width, height));
                    contentRV.setLayoutManager(new GridLayoutManager(MainUserActivity.this, 3));
                    activateToolBar();
                }
            }

            @Override
            public void onFailure(Call<NowPlaying> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void getAllMovies(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes user = retrofit.create(UserRoutes.class);
        Call<List<MoviePOJO>> call = user.getMovies(getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<MoviePOJO>>() {
            @Override
            public void onResponse(Call<List<MoviePOJO>> call, Response<List<MoviePOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<MoviePOJO> list = response.body();
                    for(MoviePOJO object : list){
                        movieList.add(new MainMovieObject(object.getId(), object.getTitle(), object.getDescription(),
                                object.getUrl(), object.getPosterPath(), object.getBackdrop(), object.getRating(),object.getLanguage(),
                                object.getCreatedAt(), object.getUploadedBy(), object.getYear()));
                    }
                    carousalVP.setAdapter(new ViewPagerAdapter(MainUserActivity.this, movieList));
                }
            }

            @Override
            public void onFailure(Call<List<MoviePOJO>> call, Throwable t) {
                mainScreenPB.setVisibility(View.GONE);
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void getAllMoviesRating(int rating){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes user = retrofit.create(UserRoutes.class);
        Call<List<MoviePOJO>> call = user.getMoviesByRating(rating, getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<MoviePOJO>>() {
            @Override
            public void onResponse(Call<List<MoviePOJO>> call, Response<List<MoviePOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<MoviePOJO> list = response.body();
                    movieList.clear();
                    for(MoviePOJO object : list){
                        movieList.add(new MainMovieObject(object.getId(), object.getTitle(), object.getDescription(),
                                object.getUrl(), object.getPosterPath(), object.getBackdrop(), object.getRating(),object.getLanguage(),
                                object.getCreatedAt(), object.getUploadedBy(), object.getYear()));
                    }
                    populateHorizontalLayout();
                }
            }

            @Override
            public void onFailure(Call<List<MoviePOJO>> call, Throwable t) {
                mainScreenPB.setVisibility(View.GONE);
                Log.e("ERROR", t.getMessage());
            }
        });

    }

    private void populateHorizontalLayout() {
        horizontalScrollView.removeAllViews();
        for(MainMovieObject movie : movieList){
            TextView textView = new TextView(this);
            if(movie.getTitle().length()>15){
                textView.setText(movie.getTitle().substring(0,14) + "...");
            }else{
                textView.setText(movie.getTitle());
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getColor(android.R.color.white));
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width/2, width/2 - 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this).load(new Config().getImageBaseUrl()+movie.getBackdropPath()).into(imageView);
            imageView.setPadding(5, 10,5, 10);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            horizontalScrollView.addView(linearLayout);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.nav_gallery:
                Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_slideshow:
                Toast.makeText(this, "Slideshow", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(MainUserActivity.this)
                        .setTitle("Logout user")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mainScreenPB.setVisibility(View.VISIBLE);
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
        Call<LogoutUser> call = user.logoutUser(getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<LogoutUser>() {
            @Override
            public void onResponse(Call<LogoutUser> call, Response<LogoutUser> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    if(response.body().getMessage().equals("logged_out")){
                        SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor sf = sharedPreferences.edit();
                        sf.remove("USER_TOKEN");
                        sf.apply();
                        mainScreenPB.setVisibility(View.GONE);
                        startActivity(new Intent(MainUserActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutUser> call, Throwable t) {
                mainScreenPB.setVisibility(View.GONE);
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

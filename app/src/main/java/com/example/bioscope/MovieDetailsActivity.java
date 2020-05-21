package com.example.bioscope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bioscope.API.TMDBRoutes;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.AuxillaryResponse;
import com.example.bioscope.POJO.MoviePOJO;
import com.example.bioscope.POJO.RecommendationPOJO;
import com.example.bioscope.Utility.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private TextView cinemaTitle, cinemaOverview, cinemalanguage, cinemaReleaseDate;
    private LinearLayout linearLayout;
    private ImageView cinemaPoster, cinemaBackdrop;
    private FloatingActionButton downloadFAB, playFAB;

    private String movieID;
    private String url;
    private boolean isCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        slidingUpPanelLayout = findViewById(R.id.movieDetailsSlider);
        cinemaTitle = findViewById(R.id.cinemaTitle);
        cinemaOverview = findViewById(R.id.cinemaOverview);
        cinemaPoster = findViewById(R.id.cinemaPoster);
        cinemaBackdrop = findViewById(R.id.cinemaBackdrop);
        cinemalanguage = findViewById(R.id.cinemaLanguage);
        cinemaReleaseDate = findViewById(R.id.cinemaRelease);
        linearLayout = findViewById(R.id.recommendationHousingLL);
        downloadFAB = findViewById(R.id.downloadFAB);
        playFAB = findViewById(R.id.watchNowFAB);
    }

    @Override
    protected void onStart() {
        super.onStart();

        movieID = getIntent().getStringExtra("movie_id");
        if(movieID!=null){
                getMovieInformation(movieID);
        }else{
            finish();
        }
        downloadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        playFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url!=null){
                    Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                    intent.putExtra("videourl",url);
                    startActivity(intent);
                }else{
                    Toast.makeText(MovieDetailsActivity.this, "Video unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float presentVal = 0f;
                if(slideOffset>presentVal)
                {
                    //panel slide state is expanded
                    if(cinemaTitle!=null && !isCalled) {
                        getRecommendation(cinemaTitle.getText().toString());
                        isCalled = true;
                    }
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    private void getMovieInformation(String movie_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes user = retrofit.create(UserRoutes.class);
        Call<MoviePOJO> call = user.getCinema(movie_id, getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<MoviePOJO>() {
            @Override
            public void onResponse(Call<MoviePOJO> call, Response<MoviePOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    MoviePOJO object = response.body();
                    populateLayout(object.getTitle(), object.getDescription(), object.getYear(), object.getPosterPath(), object.getBackdrop(), object.getUrl(), object.getLanguage());
                    url = response.body().getUrl();
                }
            }

            @Override
            public void onFailure(Call<MoviePOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void getRecommendation(String title){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();

        UserRoutes user = retrofit.create(UserRoutes.class);
        Call<List<RecommendationPOJO>> call = user.getRecommendations(title, getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<RecommendationPOJO>>() {
            @Override
            public void onResponse(Call<List<RecommendationPOJO>> call, Response<List<RecommendationPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<RecommendationPOJO> recommendationPOJOList = response.body();
                    for(RecommendationPOJO recommended : recommendationPOJOList){
                        getPosters(recommended.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RecommendationPOJO>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

    }

    private void getPosters(String title){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getOmdbBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBRoutes user = retrofit.create(TMDBRoutes.class);
        Call<AuxillaryResponse> call = user.getPosters(title, new Config().getOmdbApiKey());
        call.enqueue(new Callback<AuxillaryResponse>() {
            @Override
            public void onResponse(Call<AuxillaryResponse> call, Response<AuxillaryResponse> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    Log.i("POSTERS", response.body().getPoster());
                    ImageView imageView = new ImageView(MovieDetailsActivity.this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setPadding(2,4, 2, 4);
                    Glide.with(MovieDetailsActivity.this).load(response.body().getPoster()).into(imageView);
                    linearLayout.addView(imageView);
                }
            }

            @Override
            public void onFailure(Call<AuxillaryResponse> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void populateLayout(String title, String overview, String year, String posterPath, String backdropPath, String url, String language){
        cinemaTitle.setText(title);
        cinemaOverview.setText(overview);
        cinemaReleaseDate.setText("Release: "+year);
        if(language.equals("en")){
            cinemalanguage.setText("Language: English");
        }

        Glide.with(this).load(new Config().getImageBaseUrl() + posterPath).into(cinemaPoster);
        Glide.with(this)
                .asBitmap()
                .load(new Config().getImageBaseUrl() + backdropPath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Blurry.with(MovieDetailsActivity.this).from(resource).into(cinemaBackdrop);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

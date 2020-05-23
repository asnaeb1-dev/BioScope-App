package com.example.bioscope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.MoviePOJO;
import com.example.bioscope.POJO.RecommendationPOJO;
import com.example.bioscope.POJO.Subclass.Actor;
import com.example.bioscope.POJO.Subclass.Backdrop;
import com.example.bioscope.POJO.Subclass.GenresArray;
import com.example.bioscope.POJO.Subclass.Poster;
import com.example.bioscope.POJO.Subclass.Recommendation;
import com.example.bioscope.POJO.Subclass.Video;
import com.example.bioscope.Utility.Config;
import com.example.bioscope.Utility.MainMovieObject;
import com.example.bioscope.Utility.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private TextView cinemaTitle, cinemaOverview, cinemalanguage, cinemaReleaseDate;
    private LinearLayout actorLL, recommedationLL;
    private ImageView cinemaPoster, backButton;

    private String movieID;
    private String url;
    private boolean isCalled = false;

    private CinemaPOJO cinemaPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        slidingUpPanelLayout = findViewById(R.id.movieDetailsSlider);
        cinemaPoster = findViewById(R.id.cinemaPoster);
        backButton = findViewById(R.id.backButton);
        cinemalanguage = findViewById(R.id.languageTV);
        cinemaTitle = findViewById(R.id.cinemaTitle);
        cinemaReleaseDate = findViewById(R.id.releaseTextView);
        cinemaOverview = findViewById(R.id.cinemaOverView);
        actorLL = findViewById(R.id.actorListLL);
        recommedationLL = findViewById(R.id.recommedationLL);
        setPanelHeight();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //movieID = getIntent().getStringExtra("movie_id");
        movieID = "5ec92453d9d2f6001700fb71";
        if(movieID!=null){
            if(!isCalled){
                getMovieDetails();
                isCalled = true;
            }
        }else{
            Toast.makeText(this, "Please try again...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setPanelHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        slidingUpPanelLayout.setPanelHeight(displayMetrics.heightPixels/2 - 100);

    }

    private Retrofit initializeRetrofit(String url){
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void getMovieDetails(){
        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        Call<CinemaPOJO> call = user.getCinema(movieID, getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<CinemaPOJO>() {
            @Override
            public void onResponse(Call<CinemaPOJO> call, Response<CinemaPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    cinemaPOJO = response.body();
                    populateLayout();
                }
            }

            @Override
            public void onFailure(Call<CinemaPOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void populateLayout() {
        //set main poster
        for(Poster poster : cinemaPOJO.getPosters()){
            if(poster.getHeight() == 3000 && poster.getWidth() == 2000){
                Glide.with(this).load(new Config().getImageBaseUrl() + poster.getPosterPath()).into(cinemaPoster);
                break;
            }
        }
        actorLL.removeAllViews();
        recommedationLL.removeAllViews();
        for(Actor actor : cinemaPOJO.getActors()){
            if(actor.getActorPoster()!=null){
                final CircleImageView circleImageView = new CircleImageView(this);
                circleImageView.setLayoutParams(new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT));
                Glide.with(this)
                        .asBitmap()
                        .load(new Config().getImageBaseUrl() + actor.getActorPoster())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                circleImageView.setImageBitmap(Bitmap.createScaledBitmap(resource, 150, 150, true));
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                actorLL.addView(circleImageView);
            }
        }
        //set title
        cinemaTitle.setText(cinemaPOJO.getTitle());
        cinemaOverview.setText(cinemaPOJO.getDescription());
        cinemaReleaseDate.setText("Release: "+ cinemaPOJO.getYear());
        if( cinemaPOJO.getLanguage().equals("en")){
            cinemalanguage.setText("Language: English");
        }

        /*
        RoundedBitmapDrawable dr =
    RoundedBitmapDrawableFactory.create(res, src);
dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
imageView.setImageDrawable(dr);
         */
        for(Recommendation recommendation: cinemaPOJO.getRecommendation()){
            final ImageView imageView = new ImageView(this);
            imageView.setPadding(10, 5, 10, 5);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this)
                    .asBitmap()
                    .load(recommendation.getMoviePosterData())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),resource);
                            dr.setCornerRadius(10);
                            imageView.setImageDrawable(dr);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
            recommedationLL.addView(imageView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void goBackFunction(View view) {
        finish();
    }
}

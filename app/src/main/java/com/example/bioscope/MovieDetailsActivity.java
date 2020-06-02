package com.example.bioscope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.Subclass.Actor;
import com.example.bioscope.POJO.Subclass.GenresArray;
import com.example.bioscope.POJO.Subclass.MovieArray;
import com.example.bioscope.POJO.Subclass.Poster;
import com.example.bioscope.POJO.Subclass.Recommendation;
import com.example.bioscope.Utility.Config;
import com.example.bioscope.Utility.LastPlayed;
import com.google.android.material.snackbar.Snackbar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private TextView cinemaTitle, cinemaOverview, cinemalanguage, cinemaReleaseDate, tmdbRating;
    private LinearLayout actorLL, recommedationLL, genreLL;
    private ImageView cinemaPoster, backButton, isFavourite;
    private ConstraintLayout constraintLayout;
    private String movieID;
    private String url;
    private boolean isCalled = false;
    private ProgressDialog dialog;

    private CinemaPOJO cinemaPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        constraintLayout = findViewById(R.id.cL);
        slidingUpPanelLayout = findViewById(R.id.movieDetailsSlider);
        cinemaPoster = findViewById(R.id.cinemaPoster);
        backButton = findViewById(R.id.backButton);
        cinemalanguage = findViewById(R.id.languageTV);
        cinemaTitle = findViewById(R.id.cinemaTitle);
        cinemaReleaseDate = findViewById(R.id.releaseTextView);
        cinemaOverview = findViewById(R.id.cinemaOverView);
        actorLL = findViewById(R.id.actorListLL);
        recommedationLL = findViewById(R.id.recommedationLL);
        tmdbRating = findViewById(R.id.tmdbRating);
        genreLL = findViewById(R.id.genreLL);
        isFavourite = findViewById(R.id.isFavourite);

        setPanelHeight();
    }

    @Override
    protected void onStart() {
        super.onStart();

        movieID = getIntent().getStringExtra("movie_id");
        if(movieID!=null){
            if(!isCalled){
                dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
                dialog.show();
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

    private int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
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
                    dialog.dismiss();
                    populateLayout();
                }
            }

            @Override
            public void onFailure(Call<CinemaPOJO> call, Throwable t) {
                dialog.dismiss();
                getMovieDetails();
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void populateLayout() {
        //set main poster

        String url ="https://image.tmdb.org/t/p/original"+cinemaPOJO.getPosters().get(generateRandomNumber(0, cinemaPOJO.getPosters().size()-1)).getPosterPath();
        Glide.with(this).load(url).into(cinemaPoster);

        actorLL.removeAllViews();
        recommedationLL.removeAllViews();
        for(final Actor actor : cinemaPOJO.getActors()){
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

                circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = MovieDetailsActivity.this.getLayoutInflater().inflate(R.layout.cast_dialog_ui, null);
                        TextView name = view.findViewById(R.id.actorName),
                                characterName = view.findViewById(R.id.castCharacterName);
                        CircleImageView ci = view.findViewById(R.id.castProfilePic);
                        name.setText("Name: " +actor.getActor());
                        characterName.setText("Character: "+actor.getActorCharacter());
                        Glide.with(MovieDetailsActivity.this).load(new Config().getImageBaseUrl() + actor.getActorPoster()).into(ci);
                        new AlertDialog.Builder(MovieDetailsActivity.this)
                                .setView(view)
                                .show();
                    }
                });

            }
        }
        //set title
        cinemaTitle.setText(cinemaPOJO.getTitle());
        //set overview
        cinemaOverview.setText(cinemaPOJO.getDescription());
        //set year of release
        cinemaReleaseDate.setText("Release: "+ cinemaPOJO.getYear());
        //set language
        if( cinemaPOJO.getLanguage().equals("en")){
            cinemalanguage.setText("Language: English");
        }
        //set video
        if(cinemaPOJO.getVideos().size()>0){
        }
        //setRating
        tmdbRating.setText(cinemaPOJO.getRating());

        //set recommendation
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

        //set Genre
        for(GenresArray genre : cinemaPOJO.getGenresArray()){

            int range = 15;
            int res = (int) ( Math.random()*range);
            View view = getLayoutInflater().inflate(R.layout.genre_pod, genreLL,false);
            TextView tv = view.findViewById(R.id.genreText);
            CardView cardView = view.findViewById(R.id.genreCard);
            tv.setText(genre.getGenre());
            assignColor(res, cardView);
            genreLL.addView(view);
        }
    }

    private void assignColor(int rand ,CardView c){
        switch (rand){
            case 0:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_1));
                break;
            case 1:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_2));
                break;
            case 2:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_3));
                break;
            case 3:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_4));
                break;
            case 4:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_5));
                break;
            case 5:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_6));
                break;
            case 6:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_7));
                break;
            case 7:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_8));
                break;
            case 8:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_14));
                break;
            case 9:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_9));
                break;
            case 10:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_10));
                break;
            case 11:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_11));
                break;
            case 12:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_12));
                break;
            case 13:
                c.setCardBackgroundColor(getResources().getColor(R.color.color_13));
                break;

            default:
                break;
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

    public void addFavourite(View view) {
        Toast.makeText(this, "Favourite" , Toast.LENGTH_SHORT).show();
    }

    public void playMovie(View view) {
        //sve to database
        //play movie
        if(cinemaPOJO.getUrl()!=null){
            pushMovieWatchedToDB();
            if(cinemaPOJO.getBackdrops().get(0).getBackdropPath() != null){
                new LastPlayed(cinemaPOJO.getTitle(), cinemaPOJO.getId(), cinemaPOJO.getTmdbId(), cinemaPOJO.getPosters().get(0).getPosterPath(), cinemaPOJO.getBackdrops().get(0).getBackdropPath(), this, cinemaPOJO.getUrl());
            }else{
                new LastPlayed(cinemaPOJO.getTitle(), cinemaPOJO.getId(), cinemaPOJO.getTmdbId(), cinemaPOJO.getPosters().get(0).getPosterPath(), "", this, cinemaPOJO.getUrl());
            }

            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("video_url", cinemaPOJO.getUrl());
            startActivity(intent);
        }else{
            Toast.makeText(this, "Sorry! This movie is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void pushMovieWatchedToDB() {
        String movieid = cinemaPOJO.getId();
        String movieName = cinemaPOJO.getTitle();
        String moviePoster = cinemaPOJO.getPosters().get(0).getPosterPath();

        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);

        MovieArray movieArray = new MovieArray();
        movieArray.setMovieId(movieid);
        movieArray.setMovieName(movieName);
        if(moviePoster!=null){
            movieArray.setMoviePoster(moviePoster);
        }else{
            movieArray.setMoviePoster("");
        }


        Call<List<MovieArray>> call = user.pushMovieWatched(movieArray, getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<MovieArray>>() {
            @Override
            public void onResponse(Call<List<MovieArray>> call, Response<List<MovieArray>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                }
            }

            @Override
            public void onFailure(Call<List<MovieArray>> call, Throwable t) {
            }
        });
    }

    public void downloadMedia(View view) {
        if(cinemaPOJO.getUrl()!=null){
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(cinemaPOJO.getUrl());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
            downloadManager.enqueue(request);
            Snackbar.make(constraintLayout, "Downloading "+ cinemaPOJO.getTitle(), Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(constraintLayout, "Sorry! Download link unavailable.", Snackbar.LENGTH_SHORT).show();
        }
    }
}

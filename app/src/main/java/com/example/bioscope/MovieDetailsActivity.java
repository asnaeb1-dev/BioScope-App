package com.example.bioscope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.Subclass.Actor;
import com.example.bioscope.POJO.Subclass.GenresArray;
import com.example.bioscope.POJO.Subclass.Poster;
import com.example.bioscope.POJO.Subclass.Recommendation;
import com.example.bioscope.Utility.Config;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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

    private String movieID;
    private String url;
    private boolean isCalled = false;
    private ProgressDialog dialog;

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
        Toast.makeText(this, "Play movie", Toast.LENGTH_SHORT).show();
    }

    public void downloadMedia(View view) {
        Toast.makeText(this, "Download media", Toast.LENGTH_SHORT).show();
    }
}

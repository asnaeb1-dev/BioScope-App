package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bioscope.API.AdminRoutes;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.LogoutPOJO;
import com.example.bioscope.POJO.Subclass.GenreChoice;
import com.example.bioscope.POJO.Subclass.GenresArray;
import com.example.bioscope.POJO.Subclass.MovieArray;
import com.example.bioscope.POJO.UserPOJO;
import com.example.bioscope.Utility.Config;
import com.example.bioscope.Utility.MoviesWatchedAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

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
    private TextView profileName;
    private LinearLayout genreList;
    private RecyclerView recyclerView;
    private int height, width;
    private ProgressDialog progressDialog;

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
        profileName = findViewById(R.id.profileName);
    }

    @Override
    protected void onStart() {
        super.onStart();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        slidingUpPanelLayout.setPanelHeight(height/2 + 100);
        getProfile();

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });

    }


    private void getProfile(){
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), "Loading. Please wait...", true);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();
        UserRoutes user = retrofit.create(UserRoutes.class);
        String token = getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null);
        Call<UserPOJO> call = user.getUserProfile(token);
        call.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    populateLayout(response.body().getName(), response.body().getMovieArray(), response.body().getGenreChoices());
                }
            }

            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });

    }

    private void populateLayout(String name, List<MovieArray> moviesWatched, List<GenreChoice> genreChoices){
        profileName.setText(name);
        //set genre
        genreList.removeAllViews();
        for(GenreChoice genre : genreChoices){

            int range = 15;
            int res = (int) ( Math.random()*range);
            View view = getLayoutInflater().inflate(R.layout.genre_pod, genreList,false);
            TextView tv = view.findViewById(R.id.genreText);
            CardView cardView = view.findViewById(R.id.genreCard);
            tv.setText(genre.getGenre());
            assignColor(res, cardView);
            genreList.addView(view);
        }
        recyclerView.setAdapter(new MoviesWatchedAdapter(this, moviesWatched, height, width));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        progressDialog.dismiss();
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
}

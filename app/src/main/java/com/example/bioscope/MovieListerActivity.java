package com.example.bioscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.Utility.Config;
import com.example.bioscope.Utility.MovieListerRVAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView title;
    private ImageView backButton;

    private String category;
    private int callSc, height, width;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_lister);

        recyclerView = findViewById(R.id.movielisterRV);
        backButton = findViewById(R.id.movieListBackButton);
        title = findViewById(R.id.movieListName);

        category = getIntent().getStringExtra("_title_");
        setTitle(category);
        progressDialog = ProgressDialog.show(MovieListerActivity.this,"", "Loading. Please wait...", true);
        if(getIntent().getIntExtra("callSc", 0) == 0){
            getAllDataByCategory(category);
        }else{
            getAllDataByIndustry(category);
        }
    }

    private void setTitle(String category){
        switch(category) {
            case "superhero":
                title.setText("Marvel/DC");
                break;

            case "action":
                title.setText("Action/Thriller/Sci-fi");
                break;

            case "crime":
                title.setText("Crime/Drama");
                break;

            case "comedy":
                title.setText("Comedy/Animation");
                break;

            case "horror":
                title.setText("Horror");
                break;

            case "romance":
                title.setText("Romance");
                break;

            case "hollywood":
                title.setText("Hollywood");
                break;

            case "bollywood":
                title.setText("Bollywood");
                break;

            default:
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private Retrofit initializeRetrofit(String url){
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void getAllDataByIndustry(String toolbarTitle) {
        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        Call<List<CinemaPOJO>> call = user.getMoviesByIndustry(toolbarTitle, 100,getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<CinemaPOJO>>() {
            @Override
            public void onResponse(Call<List<CinemaPOJO>> call, Response<List<CinemaPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<CinemaPOJO> list = response.body();
                    recyclerView.setAdapter(new MovieListerRVAdapter(MovieListerActivity.this, list, height, width));
                    recyclerView.setLayoutManager(new GridLayoutManager(MovieListerActivity.this, 3));
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<CinemaPOJO>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void getAllDataByCategory(String category){
        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        Call<List<CinemaPOJO>> call = user.getMoviesByCategory(category, 100 ,getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<CinemaPOJO>>() {
            @Override
            public void onResponse(Call<List<CinemaPOJO>> call, Response<List<CinemaPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    List<CinemaPOJO> list = response.body();
                    recyclerView.setAdapter(new MovieListerRVAdapter(MovieListerActivity.this, list, height, width));
                    recyclerView.setLayoutManager(new GridLayoutManager(MovieListerActivity.this, 3));
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(MovieListerActivity.this, "Failed to fetch data. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CinemaPOJO>> call, Throwable t) {
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
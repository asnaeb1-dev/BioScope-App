package com.example.bioscope.Utility;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.bioscope.API.TMDBRoutes;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.MainUserActivity;
import com.example.bioscope.POJO.MoviePOJO;
import com.example.bioscope.POJO.TMDBAPI.NowPlaying;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBHandler {

    private Context context;

    public TMDBHandler(Context context) {
        this.context = context;
    }

}

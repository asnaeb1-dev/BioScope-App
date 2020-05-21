package com.example.bioscope.API;

import com.example.bioscope.POJO.TMDBAPI.NowPlaying;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBRoutes {

    @GET("/3/movie/popular")
    Call<NowPlaying> getNowPlaying(@Query("api_key") String apikey);

}

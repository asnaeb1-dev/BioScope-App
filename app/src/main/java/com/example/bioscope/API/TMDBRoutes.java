package com.example.bioscope.API;

import com.example.bioscope.POJO.AuxillaryResponse;
import com.example.bioscope.POJO.TMDBAPI.NowPlaying;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBRoutes {

    @GET("/3/movie/popular")
    Call<NowPlaying> getNowPlaying(@Query("api_key") String apikey);

    // http://www.omdbapi.com/?t=inception&apikey=66ef6fdc
    //
    @GET("/")
    Call<AuxillaryResponse> getPosters(@Query("t") String t, @Query("apikey") String apikey);

}

package com.example.bioscope.API;

import com.example.bioscope.POJO.LogoutUser;
import com.example.bioscope.POJO.MoviePOJO;
import com.example.bioscope.POJO.UserSignUp;
import com.example.bioscope.Utility.UploaderMovieObj;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserRoutes {

    @POST("/user/signup")
    Call<UserSignUp> createUser(@Body UserSignUp userSignUp);

    @POST("/user/login")
    Call<UserSignUp> loginUser(@Body UserSignUp userSignUp);

    @GET("/user/logout")
    Call<LogoutUser> logoutUser(@Header("Authorization") String token);

    @GET("/movie/all")
    Call<List<MoviePOJO>> getMovies(@Header("Authorization") String token);

    @GET("/movie/ratings")
    Call<List<MoviePOJO>> getMoviesByRating(@Query("r") int rating,@Header("Authorization") String token);
}

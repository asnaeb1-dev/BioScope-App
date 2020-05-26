package com.example.bioscope.API;

import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.LogoutPOJO;
import com.example.bioscope.POJO.UserPOJO;
import com.example.bioscope.POJO.UserSignUp;

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
    Call<LogoutPOJO> logoutUser(@Header("Authorization") String token);

    @GET("/movie/all")
    Call<List<CinemaPOJO>> getMovies(@Header("Authorization") String token);

    @GET("/movie/ratings")
    Call<List<CinemaPOJO>> getMoviesByRating(@Query("r") int rating,@Header("Authorization") String token);

    @GET("/movie/id")
    Call<CinemaPOJO> getCinema(@Query("id") String id, @Header("Authorization") String token);

    @GET("/user/me")
    Call<UserPOJO> getUserProfile(@Header("Authorization") String token);
}

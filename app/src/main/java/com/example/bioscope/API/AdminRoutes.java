package com.example.bioscope.API;

import com.example.bioscope.POJO.CreateAdmin;
import com.example.bioscope.POJO.LoginAdminPOJO;
import com.example.bioscope.POJO.PushClass;
import com.example.bioscope.POJO.RemoveMovieAdmin;
import com.example.bioscope.POJO.Suggestions;
import com.example.bioscope.POJO.UploadedObj;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AdminRoutes {

    @POST("/admin/create")
    Call<CreateAdmin> createAdmin(@Body CreateAdmin createAdmin, @Header("specialtoken") String specialtoken);

    @POST("/admin/login")
    Call<LoginAdminPOJO> loginAdmin(@Body LoginAdminPOJO loginAdmin, @Header("specialtoken") String specialtoken);

    @GET("/movie/title/{title}")
    Call<List<Suggestions>> getSuggestions(@Path("title") String title,  @Header("Authorization") String token);

    @POST("/admin/upload/me")
    Call<PushClass> pushMovie(@Body PushClass pushClass, @Header("Authorization") String token);

    @GET("/admin/uploads")
    Call<List<UploadedObj>> getUploadedMovies(@Header("Authorization") String token);

    @DELETE("/admin/upload/delete/{id}")
    Call<UploadedObj> removeMovie(@Path("id") String id, @Header("Authorization") String token);
}

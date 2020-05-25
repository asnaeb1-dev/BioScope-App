package com.example.bioscope.POJO.Subclass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesUploaded {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("movie_id")
    @Expose
    private String movieId;
    @SerializedName("movie_name")
    @Expose
    private String movieName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

}


package com.example.bioscope.POJO.Subclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieArray {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("movieName")
    @Expose
    private String movieName;
    @SerializedName("movie_id")
    @Expose
    private String movieId;
    @SerializedName("movie_poster")
    @Expose
    private String moviePoster;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

}

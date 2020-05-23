package com.example.bioscope.POJO.Subclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommendation {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("recommended_movie")
    @Expose
    private String recommendedMovie;
    @SerializedName("recommended_movie_genre")
    @Expose
    private String recommendedMovieGenre;
    @SerializedName("movie_poster_data")
    @Expose
    private String moviePosterData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecommendedMovie() {
        return recommendedMovie;
    }

    public void setRecommendedMovie(String recommendedMovie) {
        this.recommendedMovie = recommendedMovie;
    }

    public String getRecommendedMovieGenre() {
        return recommendedMovieGenre;
    }

    public void setRecommendedMovieGenre(String recommendedMovieGenre) {
        this.recommendedMovieGenre = recommendedMovieGenre;
    }

    public String getMoviePosterData() {
        return moviePosterData;
    }

    public void setMoviePosterData(String moviePosterData) {
        this.moviePosterData = moviePosterData;
    }
}

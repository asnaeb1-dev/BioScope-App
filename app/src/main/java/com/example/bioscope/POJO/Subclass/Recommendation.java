package com.example.bioscope.POJO.Subclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommendation {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("movie_name")
    @Expose
    private String recommendedMovie;

    @SerializedName("movie_poster")
    @Expose
    private String moviePosterData;

    public String getId() {
        return id;
    }


    public String getRecommendedMovie() {
        return recommendedMovie;
    }

    public String getMoviePosterData() {
        return moviePosterData;
    }
}

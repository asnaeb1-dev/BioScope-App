package com.example.bioscope.POJO.Subclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieArray {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("movie")
    @Expose
    private String movie;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }
}

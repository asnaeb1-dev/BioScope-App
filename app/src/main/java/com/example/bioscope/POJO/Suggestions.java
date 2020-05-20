package com.example.bioscope.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggestions {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("movieid")
    @Expose
    private Integer movieid;

    public String getTitle() {
        return title;
    }

    public Integer getMovieid() {
        return movieid;
    }
}

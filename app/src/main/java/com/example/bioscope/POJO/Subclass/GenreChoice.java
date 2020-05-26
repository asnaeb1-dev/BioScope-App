package com.example.bioscope.POJO.Subclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenreChoice {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("genre")
    @Expose
    private String genre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}

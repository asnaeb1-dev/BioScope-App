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
    @SerializedName("genre_id")
    @Expose
    private Integer genreId;

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

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

}

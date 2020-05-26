package com.example.bioscope.POJO;

import com.example.bioscope.POJO.Subclass.GenreChoice;
import com.example.bioscope.POJO.Subclass.MovieArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPOJO {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("movieArray")
    @Expose
    private List<MovieArray> movieArray = null;
    @SerializedName("genre_choices")
    @Expose
    private List<GenreChoice> genreChoices = null;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MovieArray> getMovieArray() {
        return movieArray;
    }

    public void setMovieArray(List<MovieArray> movieArray) {
        this.movieArray = movieArray;
    }

    public List<GenreChoice> getGenreChoices() {
        return genreChoices;
    }

    public void setGenreChoices(List<GenreChoice> genreChoices) {
        this.genreChoices = genreChoices;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}




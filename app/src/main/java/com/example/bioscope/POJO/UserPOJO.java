package com.example.bioscope.POJO;

import java.util.List;

import com.example.bioscope.POJO.Subclass.MovieArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private List<Object> genreChoices = null;
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

        public List<Object> getGenreChoices() {
            return genreChoices;
        }

        public void setGenreChoices(List<Object> genreChoices) {
            this.genreChoices = genreChoices;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

    }




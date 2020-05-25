package com.example.bioscope.POJO.Subclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdminPresent {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("special_token")
    @Expose
    private String specialToken;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("movies_uploaded")
    @Expose
    private List<MoviesUploaded> moviesUploaded = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;

    public AdminPresent(String specialToken, String email, String name, String password) {
        this.specialToken = specialToken;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public AdminPresent(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialToken() {
        return specialToken;
    }

    public void setSpecialToken(String specialToken) {
        this.specialToken = specialToken;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MoviesUploaded> getMoviesUploaded() {
        return moviesUploaded;
    }

    public void setMoviesUploaded(List<MoviesUploaded> moviesUploaded) {
        this.moviesUploaded = moviesUploaded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


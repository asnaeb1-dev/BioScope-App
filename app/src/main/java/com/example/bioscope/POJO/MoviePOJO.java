package com.example.bioscope.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviePOJO {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("genres_array")
    @Expose
    private List<GenresArray> genresArray = null;
    @SerializedName("director")
    @Expose
    private Object director;
    @SerializedName("backdrop")
    @Expose
    private String backdrop;
    @SerializedName("posterPath")
    @Expose
    private String posterPath;
    @SerializedName("actors")
    @Expose
    private Object actors;
    @SerializedName("uploadedBy")
    @Expose
    private String uploadedBy;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<GenresArray> getGenresArray() {
        return genresArray;
    }

    public void setGenresArray(List<GenresArray> genresArray) {
        this.genresArray = genresArray;
    }

    public Object getDirector() {
        return director;
    }

    public void setDirector(Object director) {
        this.director = director;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Object getActors() {
        return actors;
    }

    public void setActors(Object actors) {
        this.actors = actors;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public class GenresArray {

    @SerializedName("_id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    }

}
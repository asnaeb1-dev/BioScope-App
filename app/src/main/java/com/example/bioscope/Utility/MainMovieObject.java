package com.example.bioscope.Utility;

public class MainMovieObject {
    private String _id;
    private String title;
    private String description;
    private String url;
    private String posterPath;
    private String backdropPath;
    private String rating;
    private String language;
    private String createdAt;
    private String uploadedBy;
    private String[] genre;
    private String year;

    public MainMovieObject(String _id, String title, String description, String url, String posterPath, String backdropPath, String rating, String language, String createdAt, String uploadedBy, String year) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rating = rating;
        this.language = language;
        this.createdAt = createdAt;
        this.uploadedBy = uploadedBy;
        this.year = year;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getRating() {
        return rating;
    }

    public String getLanguage() {
        return language;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String[] getGenre() {
        return genre;
    }

    public String getYear() {
        return year;
    }
}

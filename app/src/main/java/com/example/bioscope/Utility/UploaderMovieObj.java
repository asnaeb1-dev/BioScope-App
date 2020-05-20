package com.example.bioscope.Utility;

public class UploaderMovieObj {

    private String _id;
    private String title;
    private String url;
    private String createdAt;
    private String posterPath;
    private String language;



    public UploaderMovieObj(String _id, String title, String url, String createdAt, String posterPath, String language) {
        this._id = _id;
        this.title = title;
        this.url = url;
        this.createdAt = createdAt;
        this.posterPath = posterPath;
        this.language = language;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getLanguage() {
        return language;
    }
}

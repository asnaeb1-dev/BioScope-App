package com.example.bioscope.Utility;

public class TMDBMovieObject {
    private String title;
    private long movieid;
    private String posterPath;
    private String backdropPath;

    public TMDBMovieObject(String title, long movieid, String posterPath, String backdropPath) {
        this.title = title;
        this.movieid = movieid;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public long getMovieid() {
        return movieid;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}

package com.example.bioscope.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PushClass {

    @SerializedName("movieid")
    @Expose
    private long movieid;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("title")
    @Expose
    private String title;

    public PushClass(long movieid, String url) {
        this.movieid = movieid;
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

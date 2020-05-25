package com.example.bioscope.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutPOJO {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name")
    @Expose
    private String name;

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}

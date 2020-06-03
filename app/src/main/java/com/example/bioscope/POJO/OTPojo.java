package com.example.bioscope.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPojo {

    @SerializedName("confirmation_code")
    @Expose
    private Integer confirmationCode;

    @SerializedName("email")
    @Expose
    private String email;

    public OTPojo(String email) {
        this.email = email;
    }

    public Integer getConfirmationCode() {
        return confirmationCode;
    }
}

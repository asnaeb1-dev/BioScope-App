package com.example.bioscope.POJO;

import com.example.bioscope.POJO.Subclass.AdminPresent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminPOJO {

    @SerializedName("adminPresent")
    @Expose
    private AdminPresent adminPresent;
    @SerializedName("token")
    @Expose
    private String token;

    public AdminPresent getAdminPresent() {
        return adminPresent;
    }

    public void setAdminPresent(AdminPresent adminPresent) {
        this.adminPresent = adminPresent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


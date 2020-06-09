package com.example.bioscope.POJO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassPOJO {

    @SerializedName("oldpassword")
    @Expose
    private String oldpassword;
    @SerializedName("newpassword")
    @Expose
    private String newpassword;

    public ChangePassPOJO(String oldpassword, String newpassword) {
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

}

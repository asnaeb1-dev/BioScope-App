package com.example.bioscope.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationSettings {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ApplicationSettings(Context context) {
        this.context = context;
        initializeSharedPreferences();
    }

    private void initializeSharedPreferences(){
        sharedPreferences = context.getSharedPreferences("APPLICATION_SETTINGS", Context.MODE_PRIVATE);
    }

    private void setEditor(){
        editor = sharedPreferences.edit();
    }
    private void applyChanges(){
        editor.apply();
    }

    public void setColorSetting(int color){
        setEditor();
        editor.putInt("COLOR_VALUE", color);
        applyChanges();
    }

    public int getColorSetting(){
        return sharedPreferences.getInt("COLOR_VALUE", 15548216);
    }
}

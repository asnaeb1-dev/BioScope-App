package com.example.bioscope.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class LastPlayed {
    private String videoName;
    private String videoID;
    private int videoTMDBID;
    private String poster;
    private String backdrop;
    private Context context;
    private String url;

    private SharedPreferences sharedPreferences;

    public LastPlayed(Context context) {
        initializeSharedPreference(context);
        getData();
    }

    public LastPlayed(String videoName, String videoID, int videoTMDBID, String poster, String backdrop, Context context, String url) {
        this.videoName = videoName;
        this.videoID = videoID;
        this.videoTMDBID = videoTMDBID;
        this.poster = poster;
        this.backdrop = backdrop;
        this.context = context;
        this.url = url;

        initializeSharedPreference(context);
        saveData();
    }

    private void initializeSharedPreference(Context context){
        sharedPreferences = context.getSharedPreferences("SAVED_MOVIES", MODE_PRIVATE);
    }

    private void saveData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", videoName);
        editor.putString("video_id", videoID);
        editor.putInt("tmdb_id", videoTMDBID);
        editor.putString("poster", poster);
        editor.putString("backdrop", backdrop);
        editor.putString("url", url);

        editor.apply();
    }

    public void getData(){

        videoID = sharedPreferences.getString("video_id", null);
        videoName = sharedPreferences.getString("title", null);
        videoTMDBID = sharedPreferences.getInt("tmdb_id", -1);
        poster = sharedPreferences.getString("poster", null);
        backdrop = sharedPreferences.getString("backdrop", null);
        url = sharedPreferences.getString("url", null);
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoID() {
        return videoID;
    }

    public int getVideoTMDBID() {
        return videoTMDBID;
    }

    public String getPoster() {
        return poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getUrl() {
        return url;
    }
}

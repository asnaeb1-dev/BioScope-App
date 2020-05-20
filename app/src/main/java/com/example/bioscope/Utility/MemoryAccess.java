package com.example.bioscope.Utility;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MemoryAccess {

    private Context context;
    private ArrayList<String> movieArr;
    private String[] projection = {"*"};

    public MemoryAccess(Context context) {
        this.context = context;
        movieArr = new ArrayList<>();
    }

    public ArrayList<String> getAllMovies(){
        Cursor detailsCursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,null,null,null);
        {
            if(detailsCursor!=null)
            {
                if(detailsCursor.moveToFirst())
                {
                    do{
                        String videoName = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME));
                        String videoLocation = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        movieArr.add(videoLocation);
                    }while(detailsCursor.moveToNext());
                }
            }
        }
        return movieArr;
    }

}

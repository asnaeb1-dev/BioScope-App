package com.example.bioscope.Utility;

import java.util.Date;
import java.util.Random;

public class Config {
    private static String BASE_URL = "https://raha-bioscope-app.herokuapp.com";
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public  String getImageBaseUrl() {
        return IMAGE_BASE_URL;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public String generateVideoName(){
        String epoch = String.valueOf(new Date().getTime());
        //delimiter- d
        String rand = String.valueOf(new Random().nextInt(10000000));
        return epoch+"d"+rand;
    }
}

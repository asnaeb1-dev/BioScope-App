package com.example.bioscope.Utility;

import java.util.Date;
import java.util.Random;

public class Config {
    private static String BASE_URL = "https://raha-bioscope-app.herokuapp.com";
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static String TMDBAPIKEY = "17d99bf38e7ffbebabfc4d9713b679a8";
    private static String TMDB_BASE_URL = "https://api.themoviedb.org";

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

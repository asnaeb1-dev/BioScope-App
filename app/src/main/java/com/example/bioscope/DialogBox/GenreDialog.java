package com.example.bioscope.DialogBox;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bioscope.MainUserActivity;
import com.example.bioscope.R;
import com.example.bioscope.Utility.GenreSelectorRV;

import java.util.ArrayList;

public class GenreDialog {

    private Context context;

    public GenreDialog(Context context) {
        this.context = context;
    }

    public void generateGenreDialog(final int width, final int height){
        new AlertDialog.Builder(context)
                .setTitle("Hi! Abhigyan")
                .setMessage("Welcome to BioScope. To continue, please select some genres of your choice so that we can show you what to watch!")
                .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        populateGenres(width, height);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Do this later", null).show();

    }

    private void populateGenres(int width, int height){
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Drawable> posters = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        genres.add("Action");
        genres.add("Adventure");
        genres.add("Comedy");
        genres.add("Horror");
        genres.add("Romance");
        genres.add("Crime");
        genres.add("War");
        genres.add("Drama");

        posters.add(context.getDrawable(R.drawable.action));
        posters.add(context.getDrawable(R.drawable.adventure));
        posters.add(context.getDrawable(R.drawable.comedy));
        posters.add(context.getDrawable(R.drawable.horror));
        posters.add(context.getDrawable(R.drawable.romance));
        posters.add(context.getDrawable(R.drawable.crime));
        posters.add(context.getDrawable(R.drawable.war));
        posters.add(context.getDrawable(R.drawable.drama));

        values.add(28);
        values.add(12);
        values.add(35);
        values.add(27);
        values.add(10749);
        values.add(80);
        values.add(10752);
        values.add(18);


        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.genre_selector_dialog, null);
        RecyclerView recyclerView = view.findViewById(R.id.genreRV);
        recyclerView.setAdapter(new GenreSelectorRV(context, genres, height, width, posters, values));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent("SELECTOR_ACTION");
                        context.sendBroadcast(intent);


                    }
                }).setNegativeButton("Cancel", null).show();
    }


}

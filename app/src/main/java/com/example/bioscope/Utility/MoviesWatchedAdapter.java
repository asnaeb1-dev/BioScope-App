package com.example.bioscope.Utility;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bioscope.MovieDetailsActivity;
import com.example.bioscope.POJO.Subclass.MovieArray;
import com.example.bioscope.R;

import java.util.ArrayList;
import java.util.List;

public class MoviesWatchedAdapter extends RecyclerView.Adapter<MoviesWatchedAdapter.Viewholder>{
    private Context context;
    private List<MovieArray> movieList;
    private int height, width;

    public MoviesWatchedAdapter(Context context, List<MovieArray> movieList, int height, int width) {
        this.context = context;
        this.movieList = movieList;
        this.height = height;
        this.width = width;
    }

    @NonNull
    @Override
    public MoviesWatchedAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.unit_ui_3, parent, false);
        //create the object of the Viewholder class down below
        MoviesWatchedAdapter.Viewholder viewholder = new MoviesWatchedAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesWatchedAdapter.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        Glide.with(context).load(new Config().getImageBaseUrl()+ movieList.get(holder.getAdapterPosition()).getMoviePoster()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("movie_id", movieList.get(holder.getAdapterPosition()).getMovieId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return movieList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Viewholder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.unit_ui_3_img);
            imageView.setLayoutParams(new ConstraintLayout.LayoutParams(width/4, width/3));
            imageView.setPadding(2,5,2,5);
        }
    }
}
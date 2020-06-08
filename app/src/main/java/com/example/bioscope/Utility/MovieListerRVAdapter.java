package com.example.bioscope.Utility;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bioscope.MovieDetailsActivity;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.R;
import java.util.List;

public class MovieListerRVAdapter extends RecyclerView.Adapter<MovieListerRVAdapter.Viewholder>{
    private Context context;
    private List<CinemaPOJO> cinemaList;
    private int height;
    private int width;

    public MovieListerRVAdapter(Context context, List<CinemaPOJO> cinemaList, int height, int width) {
        this.context = context;
        this.cinemaList = cinemaList;
        this.width = width;
        this.height = height;
    }

    @NonNull
    @Override
    public MovieListerRVAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.unit_ui_3, parent, false);
        //create the object of the Viewholder class down below
        MovieListerRVAdapter.Viewholder viewholder = new MovieListerRVAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieListerRVAdapter.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        Glide.with(context).load(new Config().getImageBaseUrl() + cinemaList.get(holder.getAdapterPosition()).getPosters().get(0).getPosterPath()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MovieDetailsActivity.class).putExtra("movie_id", cinemaList.get(holder.getAdapterPosition()).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return cinemaList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public Viewholder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.unit_ui_3_img);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(width/3, height/4 ));
            itemView.setPadding(5,5,5,5);
        }
    }
}

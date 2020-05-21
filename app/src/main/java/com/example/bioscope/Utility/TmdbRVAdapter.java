package com.example.bioscope.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bioscope.R;

import java.util.ArrayList;

public class TmdbRVAdapter extends RecyclerView.Adapter<TmdbRVAdapter.Viewholder> {
    private Context context;
    private ArrayList<TMDBMovieObject> songAl;
    private int width, height;

    public TmdbRVAdapter(Context context, ArrayList<TMDBMovieObject> songAl, int width, int height) {
        this.context = context;
        this.songAl = songAl;
        this.width = width;
        this.height = height;
    }

    @NonNull
    @Override
    public TmdbRVAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.unit_ui_3, parent, false);
        TmdbRVAdapter.Viewholder viewholder = new TmdbRVAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TmdbRVAdapter.Viewholder holder,  int position) {
        String posterPath = songAl.get(holder.getAdapterPosition()).getPosterPath();
        if(!posterPath.isEmpty()){
            Glide.with(context).load(new Config().getImageBaseUrl()+posterPath).into(holder.imageView);
        }

        //holder.imageView.setLayoutParams(new LinearLayout.LayoutParams());

    }

    @Override
    public int getItemCount() {
        return songAl.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public Viewholder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.unit_ui_3_img);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new ConstraintLayout.LayoutParams(width / 3, height / 3 - 150));
            imageView.setPadding(5,5,5,5);

        }
    }
}
package com.example.bioscope.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.bioscope.R;

import java.util.ArrayList;

public class UploaderAdapter extends RecyclerView.Adapter<UploaderAdapter.Viewholder>{
    private Context context;
    private ArrayList<UploaderMovieObj> movielist;

    public UploaderAdapter(Context context, ArrayList<UploaderMovieObj> movielist) {
        this.context = context;
        this.movielist = movielist;
    }

    @NonNull
    @Override
    public UploaderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.unit_ui_2, parent, false);
        //create the object of the Viewholder class down below
        UploaderAdapter.Viewholder viewholder = new UploaderAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UploaderAdapter.Viewholder holder, final int position) {
        final String title = movielist.get(holder.getAdapterPosition()).getTitle(),
                createdAt = movielist.get(holder.getAdapterPosition()).getCreatedAt(),
                posterPath = movielist.get(holder.getAdapterPosition()).getPosterPath(),
                url = movielist.get(holder.getAdapterPosition()).getUrl();

        Glide.with(context).load(new Config().getImageBaseUrl()+posterPath).into(holder.imageView);
        holder.title.setText(title);
        holder.createdAt.setText(createdAt.substring(0, 10));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                                .setTitle("Delete movie")
                                .setMessage("Are you sure you want to delete this movie?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent("DELETE_MOVIE_BROADCAST");
                                        intent.putExtra("position", holder.getAdapterPosition());
                                        context.sendBroadcast(intent);
                                    }
                                }).setNegativeButton("Cancel", null)
                                .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return movielist.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {
        ConstraintLayout constraintLayout;
        TextView title, createdAt;
        ImageView imageView, delete;
        public Viewholder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.unit_ui_housing_layout);
            title = itemView.findViewById(R.id.unit_ui_title);
            createdAt = itemView.findViewById(R.id.createdAtUnitUI2);
            delete = itemView.findViewById(R.id.deleteButtonunitui2);
            imageView = itemView.findViewById(R.id.unit_ui_poster);
        }
    }
}
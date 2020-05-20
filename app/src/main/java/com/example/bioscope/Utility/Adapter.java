package com.example.bioscope.Utility;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bioscope.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder>{
    private Context context;
    private ArrayList<MovieObj> songAl;

    public Adapter(Context context, ArrayList<MovieObj> songAl) {
        this.context = context;
        this.songAl = songAl;
    }

    @NonNull
    @Override
    public Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.unit_ui_1, parent, false);
        //create the object of the Viewholder class down below
        Adapter.Viewholder viewholder = new Adapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        //holder.videoName.setText(songAl.get(holder.getAdapterPosition()).getVideoName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, songAl.get(holder.getAdapterPosition()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return songAl.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout;
        TextView videoName;
        public Viewholder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.layout);
            videoName = itemView.findViewById(R.id.videoName);
        }
    }
}

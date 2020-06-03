package com.example.bioscope.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.MainUserActivity;
import com.example.bioscope.POJO.Subclass.GenreChoice;
import com.example.bioscope.POJO.UserSignUp;
import com.example.bioscope.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class GenreSelectorRV extends RecyclerView.Adapter<GenreSelectorRV.Viewholder>{
    private Context context;
    private ArrayList<String> genres;
    private ArrayList<Drawable> images;
    private ArrayList<Integer> values;
    private int height, width;

    private ArrayList<Integer> selected = new ArrayList<>();

    public GenreSelectorRV(Context context, ArrayList<String> genres, int height, int width,ArrayList<Drawable> images,ArrayList<Integer> values ) {
        this.context = context;
        this.genres = genres;
        this.height = height;
        this.width = width;
        this.images = images;
        this.values = values;
        context.registerReceiver(broadcastReceiver, new IntentFilter("SELECTOR_ACTION"));
    }

    @NonNull
    @Override
    public GenreSelectorRV.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.unit_ui_4, parent, false);
        //create the object of the Viewholder class down below
        GenreSelectorRV.Viewholder viewholder = new GenreSelectorRV.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GenreSelectorRV.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView
        int num = generateRandomNumber(0, images.size() - 1);
        assignColor(num, holder.cardView);
        holder.poster.setImageDrawable(images.get(holder.getAdapterPosition()));
        holder.videoName.setText(genres.get(holder.getAdapterPosition()));
        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.selector.getVisibility() == View.GONE){
                    holder.poster.setAlpha(0.6f);
                    holder.selector.setVisibility(View.VISIBLE);
                    selected.add(values.get(holder.getAdapterPosition()));
                }else{
                    holder.poster.setAlpha(1f);
                    holder.selector.setVisibility(View.GONE);
                    selected.remove(values.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    private int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    private void assignColor(int rand ,CardView c){
        switch (rand){
            case 0:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_1));
                break;
            case 1:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_2));
                break;
            case 2:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_3));
                break;
            case 3:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_4));
                break;
            case 4:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_5));
                break;
            case 5:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_6));
                break;
            case 6:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_7));
                break;
            case 7:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_8));
                break;
            case 8:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_14));
                break;
            case 9:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_9));
                break;
            case 10:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_10));
                break;
            case 11:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_11));
                break;
            case 12:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_12));
                break;
            case 13:
                c.setCardBackgroundColor(context.getResources().getColor(R.color.color_13));
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return genres.size();
    }

    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendGenres();
        }
    };

    private void sendGenres(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes admin = retrofit.create(UserRoutes.class);
        List<GenreChoice> gc = new ArrayList<>();
        for(int i = 0;i< selected.size();i++){
            GenreChoice genreChoice = new GenreChoice();
            genreChoice.setGenre(genres.get(values.indexOf(selected.get(i))));
            genreChoice.setGenreId(selected.get(i));
            gc.add(genreChoice);
        }
        Call<List<GenreChoice>> call = admin.insertGenres(gc, context.getSharedPreferences("MY_PREFS",MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<List<GenreChoice>>() {
            @Override
            public void onResponse(Call<List<GenreChoice>> call, Response<List<GenreChoice>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    if(response.body().size()>0){
                        Toast.makeText(context, "Done! Watch away!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Sorry! Something went wrong. Try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GenreChoice>> call, Throwable t) {
                Toast.makeText(context, "Sorry! Something went wrong. Try again later", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", t.getMessage());
            }
        });
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {
        ImageView poster, selector;
        TextView videoName;
        CardView cardView;
        public Viewholder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.imageViewGenreSelector);
            poster.setAlpha(1f);
            selector = itemView.findViewById(R.id.selectorIcon);
            videoName = itemView.findViewById(R.id.genreTitle);
            cardView = itemView.findViewById(R.id.gC);

            poster.setLayoutParams(new LinearLayout.LayoutParams(width/3 - 50, height/3 - 150));

        }
    }
}
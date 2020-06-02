package com.example.bioscope.Utility;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.Subclass.Poster;
import com.example.bioscope.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private List<Poster> cinemas;

    public ViewPagerAdapter(Activity activity, List<Poster> cinemas) {
        this.activity = activity;
        this.cinemas = cinemas;
    }

    @Override
    public int getCount() {
        return cinemas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = layoutInflater.inflate(R.layout.unit_ui_3, container, false);

        ImageView imageView = myView.findViewById(R.id.unit_ui_3_img);
        String url ="https://image.tmdb.org/t/p/original"+cinemas.get(position).getPosterPath();
        Glide.with(activity).asDrawable().load(url).into(imageView);

        container.addView(myView);
        return myView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}

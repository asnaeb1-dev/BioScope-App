package com.example.bioscope.Utility;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.bioscope.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<MainMovieObject> imageids;

    public ViewPagerAdapter(Activity activity, ArrayList<MainMovieObject> imageids) {
        this.activity = activity;
        this.imageids = imageids;
    }

    @Override
    public int getCount() {
        return imageids.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = layoutInflater.inflate(R.layout.view_pager_content, container, false);

        ImageView imageView = myView.findViewById(R.id.vpContent);
        Glide.with(activity).load(new Config().getImageBaseUrl()+imageids.get(position).getPosterPath()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        container.addView(myView);
        return myView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}

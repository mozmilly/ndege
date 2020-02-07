package com.example.ndege.units.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ndege.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<PortfolioImage> images;

    public ImagePagerAdapter(Context context){
        this.context = context;
    }

    public ImagePagerAdapter(Context context, List<PortfolioImage> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.hire_car_images);

        if (images.get(position).getImage()!=null){
            if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+images.get(position).getImage())){
                Picasso.with(context)
                        .load("https://bombaservices.pythonanywhere.com"+images.get(position).getImage())
                        .placeholder(R.drawable.place_holder)
                        .into(imageView);
            }
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}

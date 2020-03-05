package com.example.ndege.units.models;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(context, android.R.style.Theme_Light);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });

                ImageView imageView = new ImageView(context);

                try{

                    imageView.setOnTouchListener((ViewLargerImageActivity)context);
                } catch (ClassCastException ex){

                }

                Glide.with(context)
                        .load("https://bombaservices.pythonanywhere.com"+images.get(position).getImage())
                        .into(imageView);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }
        });

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

package com.example.ndege.units.models;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MySearchAdapter extends RecyclerView.Adapter<MySearchAdapter.MyViewHolder> {
    List<MenuItems> menuItemsList;
    Context context;


    //declare interface
    private OnSearchItemClicked onClick;

    public interface OnSearchItemClicked {
        void onSearchItemClick(int position);
    }
    public MySearchAdapter(List<MenuItems> menuItemsList, Context context) {
        this.menuItemsList = menuItemsList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price, was_price, percentage_off;
        public ImageView image;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.discounted_name);
            price = view.findViewById(R.id.discounted_price);
            was_price = view.findViewById(R.id.discounted_from);
            image = view.findViewById(R.id.discounted_image);
            percentage_off = view.findViewById(R.id.percentage_off);
            parent = view.findViewById(R.id.discounted_parent);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_items_dicounted_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
//        if (menuItemsList.get(i).getImage()!=null){
        if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+menuItemsList.get(i).getImage())){
            Picasso.with(context)
                    .load("https://bombaservices.pythonanywhere.com"+menuItemsList.get(i).getImage())
                    .placeholder(R.drawable.place_holder)
                    .into(myViewHolder.image);
            myViewHolder.image.setVisibility(View.VISIBLE);
        }
//        }

        myViewHolder.name.setText(menuItemsList.get(i).getItem_name());
        myViewHolder.price.setText(("Ksh."+(menuItemsList.get(i).getPrice())));
        myViewHolder.was_price.setPaintFlags(myViewHolder.was_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        myViewHolder.was_price.setText(("Was Ksh."+menuItemsList.get(i).getWas_price()));
        double difference = menuItemsList.get(i).getWas_price() - menuItemsList.get(i).getPrice();
        double percentage = difference*100/menuItemsList.get(i).getWas_price();
        myViewHolder.percentage_off.setText((percentage+"% off"));
        myViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onSearchItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    public void setOnClick(OnSearchItemClicked onClick)
    {
        this.onClick=onClick;
    }


}

package com.example.ndege.units.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ndege.R;

import java.util.List;

public class OrderMenuItemAdapter extends RecyclerView.Adapter<OrderMenuItemAdapter.MyViewHolder> {

    List<MenuItems> menuItemsList;

    Context context;

    public OrderMenuItemAdapter(List<MenuItems> menuItemsList, Context context) {
        this.menuItemsList = menuItemsList;
        this.context = context;
    }

    //declare interface
    private OnItemClicked onClick;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_menu_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (menuItemsList.get(position).getImage()!=null){
            if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+menuItemsList.get(position).getImage())){
                Glide.with(context)
                        .load("https://bombaservices.pythonanywhere.com"+menuItemsList.get(position).getImage())
                        .into(holder.image);
                holder.image.setVisibility(View.VISIBLE);
            }
        } else {
            holder.image.setVisibility(View.GONE);
        }


        holder.name.setText(menuItemsList.get(position).getItem_name());
        holder.price.setText(String.valueOf("Ksh."+(menuItemsList.get(position).getPrice()+100)));
        holder.description.setText(menuItemsList.get(position).getDescription());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price, description;
        public ImageView image;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.order_menu_item_name);
            description = view.findViewById(R.id.order_menu_item_description);
            price = view.findViewById(R.id.order_menu_item_price);
            image = view.findViewById(R.id.order_menu_item_image);
            parent = view.findViewById(R.id.order_menu_item_parent);
            image.setDrawingCacheEnabled(true);
        }

    }


    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

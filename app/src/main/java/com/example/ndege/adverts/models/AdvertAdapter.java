package com.example.ndege.adverts.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.MyViewHolder>{
    List<Advert> advertList;
    Context context;


    //declare interface
    private OnMenuItemClicked onClick;

    public interface OnMenuItemClicked {
        void onMenuItemClick(int position);
    }

    public AdvertAdapter(List<Advert> advertList, Context context) {
        this.advertList = advertList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advert_rows, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(advertList.get(position).getTitle());
        holder.description.setText(advertList.get(position).getText());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onMenuItemClick(position);
            }
        });

        if (advertList.get(position).getMy_image()!=null){
            if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+advertList.get(position).getMy_image())){
                Picasso.with(context)
                        .load("https://bombaservices.pythonanywhere.com"+advertList.get(position).getMy_image())
                        .placeholder(R.drawable.place_holder)
                        .into(holder.image);
                holder.image.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description;
        public ImageView image;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            title = view.findViewById(R.id.advert_title);
            description = view.findViewById(R.id.advert_content);
            image = view.findViewById(R.id.advert_image);
            parent = view.findViewById(R.id.advert_parent);
        }

    }

    public void setOnClick(OnMenuItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

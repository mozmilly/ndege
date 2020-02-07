package com.example.ndege.units.corecategories.models;

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

public class CoreCategoryAdapter extends  RecyclerView.Adapter<CoreCategoryAdapter.MyViewHolder>{

    List<CoreCategory> coreCategoryList;
    Context context;


    public CoreCategoryAdapter(List<CoreCategory> coreCategoryList, Context context) {
        this.coreCategoryList = coreCategoryList;
        this.context = context;
    }

    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onCategoryItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.core_category_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(coreCategoryList.get(position).getCategory_name());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onCategoryItemClick(position);
            }
        });

        if (coreCategoryList.get(position).getImage()!=null){
            if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+coreCategoryList.get(position).getImage())){
                Picasso.with(context)
                        .load("https://bombaservices.pythonanywhere.com"+coreCategoryList.get(position).getImage())
                        .placeholder(R.drawable.place_holder)
                        .into(holder.imageView);
            }
        }


    }

    @Override
    public int getItemCount() {
        return coreCategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        Button edit;
        ImageView imageView;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.core_category_name);
            imageView = view.findViewById(R.id.core_category_image);

            parent = view.findViewById(R.id.core_cat_parent);
        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

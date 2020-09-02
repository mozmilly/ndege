package com.example.ndege.units.maincategories.models;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.MyViewHolder> {

    List<MainCategory> mainCategoryList;

    private ArrayList<MainCategory> hashSetMain;

    public ArrayList<MainCategory> getHashSetMain() {
        return hashSetMain;
    }

    Context context;

    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onCategoryItemClick(int position);
    }

    public MainCategoryAdapter(List<MainCategory> mainCategoryList, Context context) {
        this.mainCategoryList = mainCategoryList;
        this.context = context;
        hashSetMain = new ArrayList<>();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        Button edit;
        ImageView imageView;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.main_category_name);
            imageView = view.findViewById(R.id.main_category_image);

            parent = view.findViewById(R.id.main_category_parent);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_category_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (mainCategoryList.get(position).getImage()!=null){
            if (URLUtil.isValidUrl(mainCategoryList.get(position).getImage())){
                Picasso.with(context)
                        .load(mainCategoryList.get(position).getImage())
                        .placeholder(R.drawable.place_holder)
                        .into(holder.imageView);
            }
        }

        holder.name.setText(mainCategoryList.get(position).getCategory_name());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onCategoryItemClick(position);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (hashSetMain.contains(mainCategoryList.get(position))){
                    v.setBackgroundColor(Color.WHITE);
                    hashSetMain.remove(mainCategoryList.get(position));
                } else {
                    v.setBackgroundColor(Color.BLUE);
                    hashSetMain.add(mainCategoryList.get(position));
                }
                Toast.makeText(context, hashSetMain.toString(), Toast.LENGTH_SHORT).show();


                return false;
            }
        });




    }

    @Override
    public int getItemCount() {
        return mainCategoryList.size();
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

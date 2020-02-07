package com.example.ndege.units.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import retrofit2.http.Url;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {
    List<MenuItems> menuItemsList;

    Context context;

    String variableString = "";
    Boolean header = false;

    //declare interface
    private OnItemClicked onClick;

    public MenuItemAdapter() {
    }

    public MenuItemAdapter(List<MenuItems> menuItemsList, Context context) {
        this.menuItemsList = menuItemsList;
        this.context = context;
    }

    public MenuItemAdapter(List<MenuItems> menuItemsList) {
        this.menuItemsList = menuItemsList;
    }

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price, description, add, negotiable, stock, min_order;
        public ImageView image;
        RelativeLayout parent;
        LinearLayout parent_image;
        Button whatsapp;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.menu_item__name);
            description = view.findViewById(R.id.menu_item_description);
            price = view.findViewById(R.id.menu_item_price);
            stock = view.findViewById(R.id.mine_stock);
            add = view.findViewById(R.id.plus);
            negotiable = view.findViewById(R.id.negotiable);
            image = view.findViewById(R.id.menu_item_image);
            parent = view.findViewById(R.id.menu_item_parent);
            parent_image = view.findViewById(R.id.image_parent);
            whatsapp = view.findViewById(R.id.share_portfolio);
            min_order = view.findViewById(R.id.min_order);
            image.setDrawingCacheEnabled(true);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_menu_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



            if (menuItemsList.get(position).getImage()!=null){
                if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+menuItemsList.get(position).getImage())){
                    Glide.with(context)
                            .load("https://bombaservices.pythonanywhere.com"+menuItemsList.get(position).getImage())
                            .into(holder.image);
                    holder.image.setVisibility(View.VISIBLE);
                }
            } else {
                holder.parent_image.setVisibility(View.GONE);
            }


            holder.name.setText(menuItemsList.get(position).getItem_name());
            holder.price.setText(String.valueOf("Ksh."+menuItemsList.get(position).getPrice()));
            holder.min_order.setText(("Atleast "+menuItemsList.get(position).getMinimum_order()+" items"));
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewLargerImageActivity.class);
                    intent.putExtra("image", menuItemsList.get(position).getImage());
                    intent.putExtra("menu_item", "true");
                    ViewCoreCategories.setMenuItems(menuItemsList.get(position));
                    context.startActivity(intent);
                }
            });
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onItemClick(position);
                }
            });

            holder.whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Drawable drawable = holder.image.getDrawable();
                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                    Bitmap bitmap = bitmapDrawable .getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);

                    Uri imgUri = Uri.parse(path);
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, menuItemsList.get(position).getItem_name()+"\n"+menuItemsList.get(position).getDescription());
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                    whatsappIntent.setType("image/jpeg");
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        context.startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {

                    }

                }
            });

        }





    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateList(List<MenuItems> list){
        menuItemsList = list;
        notifyDataSetChanged();
    }

}
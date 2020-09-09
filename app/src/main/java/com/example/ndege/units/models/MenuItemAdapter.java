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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.utils.ApiUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

import static android.content.Context.MODE_PRIVATE;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {
    List<MenuItems> menuItemsList;

    Context context;
    double extra_price = 0;

    String variableString = "";
    Boolean header = false;

    private boolean isLoaderVisible = false;
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
        LinearLayout parent_image, share;
        Button whatsapp, general;

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
            general = view.findViewById(R.id.general_share);
            image.setDrawingCacheEnabled(true);
            share = view.findViewById(R.id.share_parent);
            if (context.getSharedPreferences("pref", MODE_PRIVATE).getBoolean("is_ndege_reseller", false)) {
                share.setVisibility(View.VISIBLE);
            } else {
                share.setVisibility(View.GONE);
            }
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
                if (URLUtil.isValidUrl(menuItemsList.get(position).getImage())){
                    Glide.with(context)
                            .load(menuItemsList.get(position).getImage())
                            .into(holder.image);
                    holder.image.setVisibility(View.VISIBLE);
                }
            } else {
                holder.parent_image.setVisibility(View.GONE);
            }


            holder.name.setText(menuItemsList.get(position).getItem_name());
            if (extra_price==0) {
                UnitInterface unitInterface = ApiUtils.getUnitService(context.getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
                unitInterface.get_extra_price().enqueue(new Callback<List<ExtraPrice>>() {
                    @Override
                    public void onResponse(Call<List<ExtraPrice>> call, Response<List<ExtraPrice>> response) {
                        if (response.code() == 200) {
                            if (context.getSharedPreferences("pref", MODE_PRIVATE).getBoolean("is_ndege_reseller", false)) {
                                for (ExtraPrice extraPrice : response.body()) {

                                    if (extraPrice.getName().equalsIgnoreCase("Ndege")) {
                                        holder.price.setText(String.valueOf("Ksh." + (menuItemsList.get(position).getPrice() + extraPrice.getAmount())));
                                        extra_price = extraPrice.getAmount();
                                    }
                                }
                            } else {
                                holder.whatsapp.setVisibility(View.GONE);
                                holder.general.setVisibility(View.GONE);
                                for (ExtraPrice extraPrice : response.body()) {

                                    if (extraPrice.getName().equalsIgnoreCase("Retailer")) {
                                        holder.price.setText(String.valueOf("Ksh." + (menuItemsList.get(position).getPrice() + extraPrice.getAmount())));
                                        extra_price = extraPrice.getAmount();
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<ExtraPrice>> call, Throwable t) {

                    }
                });
            } else {
                holder.price.setText(String.valueOf("Ksh." + (menuItemsList.get(position).getPrice() + extra_price)));
            }
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


//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                        Picasso.with(context)
                                .load(menuItemsList.get(position).getImage())
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });




                }
            });

            holder.general.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Drawable drawable = holder.image.getDrawable();

                    Picasso.with(context)
                            .load(menuItemsList.get(position).getImage())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);

                                    Uri imgUri = Uri.parse(path);
                                    Intent general = new Intent(Intent.ACTION_SEND);
                                    general.setType("text/plain");
                                    general.putExtra(Intent.EXTRA_TEXT, menuItemsList.get(position).getItem_name()+"\n"+menuItemsList.get(position).getDescription());
                                    general.putExtra(Intent.EXTRA_STREAM, imgUri);
                                    general.setType("image/jpeg");
                                    general.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Intent shareIntent = Intent.createChooser(general, null);
                                    context.startActivity(shareIntent);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });

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


    public void addItems(List<MenuItems> postItems) {
        menuItemsList.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;

    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = menuItemsList.size() - 1;
        MenuItems item = getItem(position);
        if (item != null) {
            menuItemsList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        menuItemsList.clear();
        notifyDataSetChanged();
    }

    MenuItems getItem(int position) {
        return menuItemsList.get(position);
    }


}
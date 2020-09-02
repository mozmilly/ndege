package com.example.ndege.units.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.ndege.R;
import com.example.ndege.units.CheckoutActivity;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.utils.ApiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.MyViewHolder> {

    List<MyCart> menuList;

    Context context;

    SharedPreferences sharedPreferences;

    TextView totalFee, serviceFee;

    public CheckOutAdapter(List<MyCart> menuList, Context context, SharedPreferences sharedPreferences, TextView totalFee, TextView serviceFee) {
        this.menuList = menuList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.totalFee = totalFee;
        this.serviceFee = serviceFee;
    }

    public CheckOutAdapter(List<MyCart> menuList) {
        this.menuList = menuList;
    }

    public CheckOutAdapter(List<MyCart> menuList, Context context, SharedPreferences sharedPreferences) {
        this.menuList = menuList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    //declare interface
    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price;
        public TextView quantity;
        public Button add_cart, subtract_cart;
        TableRow parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.check_out_name);
            quantity = view.findViewById(R.id.check_out_quantity);

            price = view.findViewById(R.id.check_out_price);
            add_cart = view.findViewById(R.id.add_cart);
            subtract_cart = view.findViewById(R.id.subtract_cart);
            parent = view.findViewById(R.id.check_out_rows);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkout_rows, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.name.setText(menuList.get(position).getMenuItems().getItem_name());
            holder.name.setAllCaps(true);
            holder.quantity.setText(String.valueOf(menuList.get(position).getQuantity()));
        UnitInterface unitInterface = ApiUtils.getUnitService(context.getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
        unitInterface.get_extra_price().enqueue(new Callback<List<ExtraPrice>>() {
            @Override
            public void onResponse(Call<List<ExtraPrice>> call, Response<List<ExtraPrice>> response) {
                if (response.code() == 200) {
                    if (context.getSharedPreferences("pref", MODE_PRIVATE).getBoolean("is_ndege_reseller", false)){
                        for (ExtraPrice extraPrice: response.body()){

                            if (extraPrice.getName().equalsIgnoreCase("Ndege")){
                                holder.price.setText(String.valueOf(menuList.get(position).getQuantity() * (menuList.get(position).getMenuItems().getPrice() + extraPrice.getAmount())));

                            }
                        }
                    } else {
                        for (ExtraPrice extraPrice: response.body()){

                            if (extraPrice.getName().equalsIgnoreCase("Supermarket")){
                                holder.price.setText(String.valueOf(menuList.get(position).getQuantity() * (menuList.get(position).getMenuItems().getPrice() + extraPrice.getAmount())));

                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ExtraPrice>> call, Throwable t) {

            }
        });
        holder.quantity.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    try {
                        int q = Integer.parseInt(holder.quantity.getText().toString());
                        holder.price.setText(String.valueOf(q * (menuList.get(position).getMenuItems().getPrice()+100)));
                    } catch (Exception ex){
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

            holder.subtract_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<MyCart> myCartUpdate = new ArrayList<>();
                    myCartUpdate = ((CheckoutActivity)context).getArrayList();


                    for (MyCart cart: myCartUpdate){
                        if (cart.getMenuItems().getItem_name().equals(menuList.get(position).getMenuItems().getItem_name())){
                            if (cart.getQuantity()==menuList.get(position).getMenuItems().getMinimum_order()){
                                Toast.makeText(context, "You purchase less than this!!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (cart.getQuantity()>=1){
                                    cart.setQuantity(cart.getQuantity()-1);
                                } else {

                                }
                            }
                        }
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(myCartUpdate);
                    editor.putString("MY_CART", json);
                    editor.apply();

                    refresh();
                }
            });

            holder.add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<MyCart> myCartUpdate = ((CheckoutActivity)context).getArrayList();
                    for (MyCart cart: myCartUpdate){
                        if (cart.getMenuItems().getItem_name().equals(menuList.get(position).getMenuItems().getItem_name())){
                                if (cart.getQuantity()>=0){
                                    cart.setQuantity(cart.getQuantity()+1);
                                } else {

                                }

                        }
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(myCartUpdate);
                    editor.putString("MY_CART", json);
                    editor.apply();

                    refresh();

                }
            });
    }

    private void refresh(){
        Intent intent = ((CheckoutActivity) context).getIntent();
        ((CheckoutActivity) context).finish();
        ((CheckoutActivity) context).overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ((CheckoutActivity) context).finish();
        ((CheckoutActivity) context).overridePendingTransition(0, 0);
        ((CheckoutActivity) context).startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

}

package com.example.ndege.units.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ndege.R;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder>{
    List<MyOrder> myOrderList;

    //declare interface
    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public MyOrderAdapter(List<MyOrder> myOrderList) {
        this.myOrderList = myOrderList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price, status, orderList, ref, delivery;
        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.my_order__rest);
            price = view.findViewById(R.id.my_order_price);
            status = view.findViewById(R.id.my_order_status);
            orderList = view.findViewById(R.id.my_order_list);
            ref = view.findViewById(R.id.my_order_ref);
            delivery = view.findViewById(R.id.my_order_delivery_fee);

            parent = view.findViewById(R.id.my_order_parent);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_order_rows, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(myOrderList.get(position).getUnit().getUnit_name());
        holder.price.setText(String.valueOf("Ksh."+myOrderList.get(position).getPrice()));
        holder.status.setText(String.valueOf(myOrderList.get(position).getStatus()));
        holder.ref.setText(String.valueOf("Ref Code: "+myOrderList.get(position).getRef_code()));
        holder.orderList.setText(String.valueOf("Order List: "+myOrderList.get(position).getOrder_name()));
        holder.delivery.setText(("Delivery Fee: Ksh."+myOrderList.get(position).getTransportation_fee()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

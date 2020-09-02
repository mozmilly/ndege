package com.example.ndege.units.orders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ndege.R;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.OrderInterface;
import com.example.ndege.units.models.MyOrder;
import com.example.ndege.units.models.MyOrderAdapter;
import com.example.ndege.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutSuccess extends AppCompatActivity implements MyOrderAdapter.OnItemClicked{
    OrderInterface orderInterface;
    MyOrderAdapter myOrderAdapter;
    private RecyclerView recyclerView;
    ProgressDialog dialog;

    private static MyOrder myOrder;

    public static MyOrder getMyOrder() {
        return myOrder;
    }

    public static void setMyOrder(MyOrder myOrder) {
        CheckOutSuccess.myOrder = myOrder;
    }

    List<MyOrder> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_success);
        recyclerView = findViewById(R.id.my_order);
        dialog = ProgressDialog.show(CheckOutSuccess.this, "Fetching the orders", "Please wait ...", true);



        orderInterface = ApiUtils.getOrderService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
        SharedPreferences sp = getSharedPreferences("pref", 0);
        String username = sp.getString("user", "no user");
        orderInterface.get_my_orders(username).enqueue(new Callback<List<MyOrder>>() {
            @Override
            public void onResponse(Call<List<MyOrder>> call, Response<List<MyOrder>> response) {
                orders = response.body();


                if (orders!=null) {

                    if (orders.isEmpty()){
                        AlertDialog alertDialog1 = new AlertDialog.Builder(CheckOutSuccess.this).create();
                        alertDialog1.setMessage("You have not made any orders. You can make one now!");
                        alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        CheckOutSuccess.super.onBackPressed();

                                    }
                                });
                        alertDialog1.show();
                    }
                    myOrderAdapter = new MyOrderAdapter(orders);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(myOrderAdapter);

                    myOrderAdapter.setOnClick(CheckOutSuccess.this);

                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<MyOrder>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CheckOutSuccess.this, ViewCoreCategories.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(CheckOutSuccess.this, ViewOrderDetailsActivity.class);
        setMyOrder(orders.get(position));
        startActivity(intent);


    }
}

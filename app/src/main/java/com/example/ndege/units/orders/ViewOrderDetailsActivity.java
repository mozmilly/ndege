package com.example.ndege.units.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ndege.R;
import com.example.ndege.units.interfaces.OrderInterface;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.MenuItemAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.MyOrder;
import com.example.ndege.units.models.OrderExtra;
import com.example.ndege.units.models.OrderMenuItemAdapter;
import com.example.ndege.units.subcorecategories.ViewSubCoreCategories;
import com.example.ndege.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderDetailsActivity extends AppCompatActivity implements OrderMenuItemAdapter.OnItemClicked {
    MyOrder myOrder;
    TextView order_name, delivery_fee, price, client_name, client_phone, margin, was_paid;
    Button call;
    RecyclerView recyclerView;
    OrderMenuItemAdapter orderMenuItemAdapter;
    List<MenuItems> menuItemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details);

        myOrder = CheckOutSuccess.getMyOrder();

        order_name = findViewById(R.id.order_name);
        delivery_fee = findViewById(R.id.order_delivery_fee);
        price = findViewById(R.id.order_items_price);
        client_name = findViewById(R.id.my_client_name);
        client_phone = findViewById(R.id.my_client_phone);
        margin = findViewById(R.id.myclient_margin);
        was_paid = findViewById(R.id.myclient_paid);

        call = findViewById(R.id.call_my_client);

        recyclerView = findViewById(R.id.order_details_recycler);

        order_name.setText(myOrder.getOrder_name());
        delivery_fee.setText(("Delivery Fee: "+myOrder.getTransportation_fee()));
        price.setText(("Items Price: "+myOrder.getPrice()));

        OrderInterface orderInterface = ApiUtils.getOrderService();

        orderInterface.get_order_extra(myOrder.getId()).enqueue(new Callback<OrderExtra>() {
            @Override
            public void onResponse(Call<OrderExtra> call, Response<OrderExtra> response) {
                if (response.code()==200){

                    OrderExtra orderExtra = response.body();


                    if (orderExtra != null) {
                        if (!orderExtra.getClient_phone().isEmpty()){

                            client_name.setText(("Client Name: "+orderExtra.getCustomer_name()));
                            client_phone.setText(("Client Phone: "+orderExtra.getClient_phone()));
                            margin.setText(("My Margin: "+orderExtra.getMargin()));
                            was_paid.setText(("Was Paid: "+orderExtra.isReseller_paid()));
                        } else {
                            client_name.setVisibility(View.GONE);
                            margin.setVisibility(View.GONE);
                            client_phone.setVisibility(View.GONE);
                            was_paid.setVisibility(View.GONE);
                        }
                    } else {
                        client_name.setVisibility(View.GONE);
                        margin.setVisibility(View.GONE);
                        client_phone.setVisibility(View.GONE);
                        was_paid.setVisibility(View.GONE);
                    }


                } else if (response.code()==404){
                    client_name.setVisibility(View.GONE);
                    margin.setVisibility(View.GONE);
                    client_phone.setVisibility(View.GONE);
                    was_paid.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<OrderExtra> call, Throwable t) {

            }
        });


        orderInterface.get_order_menu_items(myOrder.getId()).enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();



                    orderMenuItemAdapter = new OrderMenuItemAdapter(menuItemsList, ViewOrderDetailsActivity.this);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(orderMenuItemAdapter);
                    orderMenuItemAdapter.setOnClick(ViewOrderDetailsActivity.this);
                    orderMenuItemAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<MenuItems>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {

    }
}

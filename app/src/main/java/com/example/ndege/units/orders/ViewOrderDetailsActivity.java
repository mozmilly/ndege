package com.example.ndege.units.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndege.R;
import com.example.ndege.units.corecategories.ViewCoreCategories;
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
    Button call, whatsapp;
    RecyclerView recyclerView;
    OrderMenuItemAdapter orderMenuItemAdapter;
    List<MenuItems> menuItemsList;

    private static final int REQUEST_PHONE_CALL = 1;

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
        whatsapp = findViewById(R.id.whatsapp);

        recyclerView = findViewById(R.id.order_details_recycler);

        order_name.setText(myOrder.getOrder_name());
        delivery_fee.setText(("Delivery Fee: "+myOrder.getTransportation_fee()));
        price.setText(("Items Price: "+myOrder.getPrice()));

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "0753540580"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        if (ContextCompat.checkSelfPermission(ViewOrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ViewOrderDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                        }
                        else
                        {
                            startActivity(intent);
                        }

                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String url = "https://wa.me/254753540580";
                intentWhatsapp.setData(Uri.parse(url));
                intentWhatsapp.setPackage("com.whatsapp");
                startActivity(intentWhatsapp);
            }
        });
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
        if (myOrder.getStatus().equalsIgnoreCase("Delivered")){
            Intent intent = new Intent(ViewOrderDetailsActivity.this, CommentOnProduct.class);
            intent.putExtra("product_id", menuItemsList.get(position).getId());
            intent.putExtra("image", menuItemsList.get(position).getImage());
            ViewCoreCategories.setMenuItems(menuItemsList.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else
                {

                }
                return;
            }
        }
    }
}

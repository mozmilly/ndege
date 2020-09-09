package com.example.ndege.units;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ndege.JSONParserTask;
import com.example.ndege.R;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.CheckOutInterface;
import com.example.ndege.units.interfaces.FeeInterface;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.CheckOutAdapter;
import com.example.ndege.units.models.ExtraPrice;
import com.example.ndege.units.models.Fee;
import com.example.ndege.units.models.LocationName;
import com.example.ndege.units.models.LocationPrice;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.MyCart;
import com.example.ndege.units.orders.CheckOutSuccess;
import com.example.ndege.utils.ApiUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements CheckOutAdapter.OnItemClicked{
    private RecyclerView recyclerView;
    CheckOutAdapter checkOutAdapter;
    private List<MyCart> arrayList;
    private SharedPreferences sharedPreferences, sharedPreferences2;

    TextView deliveryFee, marginText;

    List<Fee> feeList;


    CheckOutInterface checkOutInterface;

    public List<MyCart> getArrayList() {
        return arrayList;
    }
    FeeInterface feeInterface;
    double transport_fee = 0;
    double total_fee=0;
    String unit_name;
    double total_price;
    int total_quantity = 0;

    LocationName locationName;
    double margin = 0;
    Spinner locationSpinner;
    double ndege_extra = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        locationSpinner = findViewById(R.id.location_name_spinner);

        marginText = findViewById(R.id.checkout_margin);
        marginText.setText(("Ksh."+getIntent().getStringExtra("margin")));

        try{

            margin = Double.parseDouble(getIntent().getStringExtra("margin"));
        } catch (Exception ex){

        }

        UnitInterface unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));


        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationName = (LocationName) adapterView.getSelectedItem();

                SharedPreferences sharedPreferences = getSharedPreferences("Cart", 0);
                String town = sharedPreferences.getString("location_town", "Nakuru");

                unitInterface.compute_location_price(town, locationName.getName()).enqueue(new Callback<LocationPrice>() {
                    @Override
                    public void onResponse(Call<LocationPrice> call, Response<LocationPrice> response) {
                        if (response.code()==200){
                            transport_fee = response.body().getPrice();
                            deliveryFee.setText(String.valueOf(transport_fee));
                            TextView total = findViewById(R.id.checkout_total_fee);
                            double my_estimate = transport_fee+total_fee+margin;
                            total.setText(String.valueOf("Ksh."+(my_estimate)));

                        }
                    }

                    @Override
                    public void onFailure(Call<LocationPrice> call, Throwable throwable) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        Location Name Spinner



        unitInterface.get_location_names().enqueue(new Callback<List<LocationName>>() {
            @Override
            public void onResponse(Call<List<LocationName>> call, Response<List<LocationName>> response) {
                if (response.code()==200){
                    List<LocationName> locationNameList = response.body();
                    ArrayAdapter<LocationName> dataAdapter = new ArrayAdapter<>(CheckoutActivity.this, android.R.layout.simple_spinner_item, locationNameList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(dataAdapter);

                    locationName = (LocationName) locationSpinner.getSelectedItem();

                    SharedPreferences sharedPreferences = getSharedPreferences("Cart", 0);
                    String town = sharedPreferences.getString("location_town", "Nakuru");

                    unitInterface.compute_location_price(town, locationName.getName()).enqueue(new Callback<LocationPrice>() {
                        @Override
                        public void onResponse(Call<LocationPrice> call, Response<LocationPrice> response) {
                            if (response.code()==200){
                                transport_fee = response.body().getPrice();
                                deliveryFee.setText(String.valueOf(transport_fee));

                                TextView total = findViewById(R.id.checkout_total_fee);
                                double my_estimate = transport_fee+total_fee+margin;
                                total.setText(String.valueOf("Ksh."+(my_estimate)));

                            }
                        }

                        @Override
                        public void onFailure(Call<LocationPrice> call, Throwable throwable) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<LocationName>> call, Throwable throwable) {

            }
        });


        EditText desc = findViewById(R.id.description);

        SharedPreferences sp1 = getSharedPreferences("pref", 0);
        deliveryFee = findViewById(R.id.check_out_delivery_fee);

        TextView textView = findViewById(R.id.service_label);
        textView.setVisibility(View.GONE);



        feeInterface = ApiUtils.getFeeService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));

        sharedPreferences2 = getSharedPreferences("Cart", 0);
        unit_name = sharedPreferences2.getString("unit_name", "");
        recyclerView = findViewById(R.id.check_out_items);


        sharedPreferences = getSharedPreferences("Cart", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MY_CART", "");


        Button btn = findViewById(R.id.final_checkout);





        Type type = new TypeToken<List<MyCart>>() {}.getType();
        if (json.equals("")){
            AlertDialog alertDialog1 = new AlertDialog.Builder(CheckoutActivity.this).create();
            alertDialog1.setMessage("You don't have items in your cart first add");
            alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(CheckoutActivity.this, ViewCoreCategories.class);
                            startActivity(intent);
                        }
                    });
            alertDialog1.show();
        } else {
            arrayList = gson.fromJson(json, type);

            unitInterface.get_extra_price().enqueue(new Callback<List<ExtraPrice>>() {
                @Override
                public void onResponse(Call<List<ExtraPrice>> call, Response<List<ExtraPrice>> response) {
                    if (response.code()==200){
                        if (getSharedPreferences("pref", Context.MODE_PRIVATE).getBoolean("is_ndege_reseller", false)) {

                            for (ExtraPrice extraPrice : response.body()) {
                                if (extraPrice.getName().equalsIgnoreCase("Ndege")) {
                                    ndege_extra = extraPrice.getAmount();

                                    double price = 0;
                                    for (MyCart myCart : arrayList) {
                                        price = myCart.getQuantity() * (myCart.getMenuItems().getPrice() + ndege_extra);
                                        total_fee += price;
                                        total_quantity += myCart.getQuantity();
                                    }

                                    TextView tv = findViewById(R.id.check_out_items_fee);
                                    tv.setText(String.valueOf("Ksh." + (total_fee + margin)));

                                }
                            }
                        } else {
                            for (ExtraPrice extraPrice : response.body()) {
                                if (extraPrice.getName().equalsIgnoreCase("Retailer")) {
                                    ndege_extra = extraPrice.getAmount();

                                double price = 0;
                                for (MyCart myCart : arrayList) {
                                    price = myCart.getQuantity() * (myCart.getMenuItems().getPrice()+ndege_extra);
                                    total_fee += price;
                                    total_quantity += myCart.getQuantity();
                                }
                                TextView tv = findViewById(R.id.check_out_items_fee);
                                tv.setText(String.valueOf("Ksh."+(total_fee+margin)));

                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<ExtraPrice>> call, Throwable t) {

                }
            });


            SharedPreferences sp = getSharedPreferences("pref", 0);
            String username = sp.getString("user", "no user");

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    checkOutInterface = ApiUtils.getCheckOutService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));


                    SharedPreferences sp2 = getSharedPreferences("Location", 0);


                        AlertDialog alertDialog1 = new AlertDialog.Builder(CheckoutActivity.this).create();
                        alertDialog1.setMessage("Do you want to make this order?");
                        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        MultipartBody.Part fileToUpload = null;
                                        RequestBody filename = null;
                                        if (getIntent().getExtras().get("file")!=null){
                                            File file = (File) getIntent().getExtras().get("file");
                                            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                                            filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                                            fileToUpload  = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                                        } else {
                                            RequestBody mfile = RequestBody.create(MultipartBody.FORM,"");
                                            fileToUpload = MultipartBody.Part.createFormData("file","",mfile);
                                            filename = RequestBody.create(MediaType.parse("text/plain"), "None");
                                        }



                                        RequestBody json_body = RequestBody.create(MediaType.parse("text/plain"), json);
                                        RequestBody username_body = RequestBody.create(MediaType.parse("text/plain"), username);
                                        RequestBody transport_body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transport_fee));
                                        RequestBody loc_body = RequestBody.create(MediaType.parse("text/plain"), locationName.getName());
                                        RequestBody desc_body = RequestBody.create(MediaType.parse("text/plain"), desc.getText().toString().trim());
                                        if (!getSharedPreferences("pref", Context.MODE_PRIVATE).getBoolean("is_ndege_reseller", false)){
                                            if (total_quantity<2){
                                                Toast.makeText(CheckoutActivity.this, "You need to have atleast 2 items in the cart!", Toast.LENGTH_LONG).show();
                                            } else {
                                                checkOut(json_body, username_body, transport_body , loc_body,desc_body, fileToUpload, filename);

                                            }
                                        } else {
                                            checkOut(json_body, username_body, transport_body ,  loc_body,desc_body, fileToUpload, filename);

                                        }
                                    }
                                });
                        alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog1.show();
                        alertDialog1.getWindow().setGravity(Gravity.BOTTOM);
                        alertDialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                }
            });

            checkOutAdapter = new CheckOutAdapter(arrayList, CheckoutActivity.this, sharedPreferences);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(checkOutAdapter);
        }

    }

    @Override
    public void onItemClick(int position) {

    }

    public void checkOut(RequestBody myCart, RequestBody username, RequestBody total_price, RequestBody loc_body,RequestBody desc, MultipartBody.Part file, RequestBody file_name){
        checkOutInterface.make_order(myCart, username, total_price, loc_body,
                desc, RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra("client_name")),
                RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra("client_phone")),
                RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra("margin")), RequestBody.create(MediaType.parse("text/plain"),"ndege"), file, file_name).enqueue(new Callback<MenuItems>() {
            @Override
            public void onResponse(Call<MenuItems> call, Response<MenuItems> response) {
                if (response.code()==200){
                    SharedPreferences settings = getSharedPreferences("Cart", 0);
                    settings.edit().clear().apply();
                    Intent intent = new Intent(CheckoutActivity.this, CheckOutSuccess.class);
                    startActivity(intent);

                    SharedPreferences sp = getSharedPreferences("pref", 0);
                    String username = sp.getString("user", "no user");

                }

            }

            @Override
            public void onFailure(Call<MenuItems> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CheckoutActivity.this, ViewCoreCategories.class);
        startActivity(intent);
    }
}

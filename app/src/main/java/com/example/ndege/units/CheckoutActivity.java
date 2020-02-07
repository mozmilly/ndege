package com.example.ndege.units;

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
import com.example.ndege.units.models.Fee;
import com.example.ndege.units.models.LocationName;
import com.example.ndege.units.models.LocationPrice;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.MyCart;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements CheckOutAdapter.OnItemClicked{
    private RecyclerView recyclerView;
    CheckOutAdapter checkOutAdapter;
    private List<MyCart> arrayList;
    private SharedPreferences sharedPreferences, sharedPreferences2;

    TextView serviceFee, deliveryFee, marginText;

    List<Fee> feeList;

    double distance1;
    CheckOutInterface checkOutInterface;

    public List<MyCart> getArrayList() {
        return arrayList;
    }
    FeeInterface feeInterface;
    double service_fee = 0;
    double transport_fee = 0;
    double total_fee=0;
    double distance = 0;
    double base = 0;
    String unit_name;
    LatLng placeSelected;
    String placeName;
    double total_price;
    double total_distance;


    TextView place;

    double margin = 0;
    Spinner locationSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        locationSpinner = findViewById(R.id.location_name_spinner);

        marginText = findViewById(R.id.checkout_margin);
        marginText.setText(("Ksh."+getIntent().getStringExtra("margin")));

        margin = Double.parseDouble(getIntent().getStringExtra("margin"));




        UnitInterface unitInterface = ApiUtils.getUnitService();

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LocationName locationName = (LocationName) adapterView.getSelectedItem();

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

                    LocationName locationName = (LocationName) locationSpinner.getSelectedItem();

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

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyB7LQ2ep5cZqRk3b63ylNe1CWDbkazR2OA");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_checkout);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setCountry("KE");
        autocompleteFragment.setHint("Delivery Point?");
        ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(15.0f);
        // Set up a PlaceSelectionListener to handle the response.
        ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setHintTextColor(Color.BLACK);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = getDrawable(R.drawable.border);
            ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setBackground(drawable);

        }
        SharedPreferences sp1 = getSharedPreferences("pref", 0);
        placeSelected =  new LatLng(Double.parseDouble(Objects.requireNonNull(sp1.getString("my_latitude", "0"))), Double.parseDouble(Objects.requireNonNull(sp1.getString("my_longitude", "0"))));
        autocompleteFragment.setText(sp1.getString("my_loc_name", "Unnamed Road"));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                sharedPreferences2 = getSharedPreferences("Cart", 0);
                double lat = Double.parseDouble(sharedPreferences2.getString("latitude", ""));
                double longitude = Double.parseDouble(sharedPreferences2.getString("longitude", ""));


                placeSelected = place.getLatLng();
                placeName = place.getName();

                String str_origin = "origin=" + lat + "," + longitude;
                String str_dest = "destination=" + place.getLatLng().latitude + "," + place.getLatLng().longitude;
                String sensor = "sensor=false";
                String mode = "mode = driving";
                String key = "key=AIzaSyA_l8XHE9Efm1op5uba-4Kzjev9KwBz1Dg";
                String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + key;
                String output = "json";
                String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


                Log.d("onMapClick", url.toString());
                FetchUrl FetchUrl = new FetchUrl();
                try {
                    String data = FetchUrl.execute(url).get();

                    JSONObject obj = new JSONObject(data);

                    JSONArray jsonChild = (JSONArray) obj.get("routes");
                    JSONObject jsonObject2 = (JSONObject) jsonChild.get(0);
                    JSONArray jsonObject3 = (JSONArray) jsonObject2.get("legs");
                    JSONObject elementObj = (JSONObject) jsonObject3.get(0);
                    JSONObject distanceObj = (JSONObject) elementObj.get("distance");
                    String distance = distanceObj.getString("text");
                    String dist = distance.replaceAll("[^\\d.]", "");
                    String nameOfDistance = distance.replace(dist, "").trim();
                    distance1 = Double.parseDouble(dist);
                    if (nameOfDistance.equals("m")) {
                        distance1 = distance1 / 1000;
                    }

                    total_price = base+distance1*transport_fee;

                    deliveryFee.setText(String.valueOf("Ksh."+total_price));
                    TextView total = findViewById(R.id.checkout_total_fee);
                    total.setText(String.valueOf("Ksh."+(total_fee+total_price)));
//                    price.setText(String.valueOf("Price: Ksh."+total_price));

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: " + status);
            }
        });

        serviceFee = findViewById(R.id.checkout_service_fee);
        deliveryFee = findViewById(R.id.check_out_delivery_fee);

        TextView textView = findViewById(R.id.service_label);
        textView.setVisibility(View.GONE);



        feeInterface = ApiUtils.getFeeService();

        sharedPreferences2 = getSharedPreferences("Cart", 0);
        unit_name = sharedPreferences2.getString("unit_name", "");

        feeInterface.get_fees().enqueue(new Callback<List<Fee>>() {
            @Override
            public void onResponse(Call<List<Fee>> call, Response<List<Fee>> response){
                feeList = response.body();

                SharedPreferences sharedPreferences = getSharedPreferences("Cart", 0);
                distance = Double.parseDouble(sharedPreferences.getString("distance", "0"));

                for (Fee fee: feeList){
                    if (fee.getFee_name().equals("Service")){
                        service_fee = fee.getFee_amount();
                    } else if (fee.getFee_name().equals("Transport")){
                        transport_fee = fee.getFee_amount();
                    } else if (fee.getFee_name().equals("Base")){
                        base = fee.getFee_amount();
                    }
                }
                serviceFee.setText(String.valueOf("Ksh."+service_fee));
                serviceFee.setVisibility(View.GONE);
                double y = transport_fee*distance+base;
                deliveryFee.setText(String.valueOf("Ksh."+total_price));
                TextView total = findViewById(R.id.checkout_total_fee);
                total.setText(String.valueOf("Ksh."+(total_fee+total_price)));

            }

            @Override
            public void onFailure(Call<List<Fee>> call, Throwable t) {

            }
        });


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
                            CheckoutActivity.super.onBackPressed();
                        }
                    });
            alertDialog1.show();
        } else {
            arrayList = gson.fromJson(json, type);


            double price = 0;
            for (MyCart myCart : arrayList) {
                price = myCart.getQuantity() * myCart.getMenuItems().getPrice();
                total_fee += price;
            }

            TextView tv = findViewById(R.id.check_out_items_fee);
            tv.setText(String.valueOf("Ksh."+total_fee+transport_fee));
            SharedPreferences sp = getSharedPreferences("pref", 0);
            String username = sp.getString("user", "no user");

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkOutInterface = ApiUtils.getCheckOutService();


                    SharedPreferences sp2 = getSharedPreferences("Location", 0);
//                    String name = sp2.getString("name", "");
//                    double latitude = Double.parseDouble(sp2.getString("loc_lat", ""));
//                    double longitude = Double.parseDouble(sp2.getString("loc_long", ""));
                    if (placeSelected!=null){
                        double latitude = placeSelected.latitude;
                        double longitude = placeSelected.longitude;
                        String name = placeName;

                        AlertDialog alertDialog1 = new AlertDialog.Builder(CheckoutActivity.this).create();
                        alertDialog1.setMessage("Do you want to make this order?");
                        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        checkOut(json, username, transport_fee , name, latitude, longitude, desc.getText().toString().trim());
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
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Please select a drop point!!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            Button order_whatsapp = findViewById(R.id.whatsapp_order);

            order_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Uri mUri = Uri.parse("smsto:+254753540580");

                    StringBuilder order = new StringBuilder();

                    for (MyCart myCart: arrayList){
                        order.append(myCart.getMenuItems().getItem_name()).append(" ").append(myCart.getQuantity()).append(",").append("\n");
                    }
                    String unit_name = sharedPreferences.getString("unit_name", "");
                    String whatsAppMessage = "Order: "+"\n"+order.toString()+"User Phone:"+username+"\n"+"Drop Location: "+placeName+"\n"+ "Total Fee:"+String.valueOf(total_fee + transport_fee*distance+base)+"\n"+"Description: "+desc.getText().toString().trim() +"\n"+"Store Name:"+unit_name;
//                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:+254753540580"+ "?body="+ whatsAppMessage));

                    String mobile = "+254753540580";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + whatsAppMessage)));

                    SharedPreferences settings = getSharedPreferences("Cart", 0);

                    settings.edit().clear().apply();

//                    Intent intent = new Intent(CheckoutActivity.this, CheckOutSuccess.class);
//                    startActivity(intent);
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

    public void checkOut(String myCart, String username, double total_price, String name, double latitude, double longitude, String desc){
        checkOutInterface.make_order(myCart, username, total_price, name, latitude, longitude,
                desc, getIntent().getStringExtra("client_name"),
                getIntent().getStringExtra("client_phone"),
                Integer.parseInt(getIntent().getStringExtra("margin"))).enqueue(new Callback<MenuItems>() {
            @Override
            public void onResponse(Call<MenuItems> call, Response<MenuItems> response) {
                Toast.makeText(CheckoutActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                if (response.code()==200){
                    SharedPreferences settings = getSharedPreferences("Cart", 0);
                    settings.edit().clear().apply();
                    Intent intent = new Intent(CheckoutActivity.this, ViewCoreCategories.class);
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

    private class  FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class  ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                JSONParserTask parser = new JSONParserTask();
                Log.d("ParserTask", parser.toString());
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);


            }

        }
    }

}

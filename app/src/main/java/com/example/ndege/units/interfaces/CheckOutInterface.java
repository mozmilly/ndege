package com.example.ndege.units.interfaces;

import com.example.ndege.units.models.MenuItems;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CheckOutInterface {

    @Multipart
    @POST("units/make_order/")
    Call<MenuItems> make_order(
            @Part("items") RequestBody my_cart,
            @Part("username") RequestBody username,
            @Part("total_price") RequestBody total_price,
            @Part("loc_name") RequestBody loc_name,
            @Part("loc_lat") RequestBody loc_lat,
            @Part("loc_long") RequestBody loc_long,
            @Part("description") RequestBody description,
            @Part("client_name") RequestBody client_name,
            @Part("client_phone") RequestBody client_phone,
            @Part("margin") RequestBody margin,
            @Part("app_id") RequestBody app_id,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name
    );
}

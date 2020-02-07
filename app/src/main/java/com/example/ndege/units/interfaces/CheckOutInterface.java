package com.example.ndege.units.interfaces;

import com.example.ndege.units.models.MenuItems;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CheckOutInterface {

    @FormUrlEncoded
    @POST("units/make_order/")
    Call<MenuItems> make_order(
            @Field("items") String my_cart,
            @Field("username") String username,
            @Field("total_price") Double total_price,
            @Field("loc_name") String name,
            @Field("loc_lat") Double loc_lat,
            @Field("loc_long") Double loc_long,
            @Field("description") String description,
            @Field("client_name") String client_name,
            @Field("client_phone") String client_phone,
            @Field("margin") int margin
    );
}

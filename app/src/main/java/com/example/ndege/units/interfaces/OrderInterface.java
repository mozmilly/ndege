package com.example.ndege.units.interfaces;

import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.MyOrder;
import com.example.ndege.units.models.OrderExtra;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderInterface {
    @FormUrlEncoded
    @POST("units/get_orders/")
    Call<List<MyOrder>> get_my_orders(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("units/get_order_extra/")
    Call<OrderExtra> get_order_extra(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("units/get_order_menu_items/")
    Call<List<MenuItems>> get_order_menu_items(
            @Field("id") int id
    );
}

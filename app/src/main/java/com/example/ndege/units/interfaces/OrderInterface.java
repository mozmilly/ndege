package com.example.ndege.units.interfaces;

import com.example.ndege.units.models.MyOrder;

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
}

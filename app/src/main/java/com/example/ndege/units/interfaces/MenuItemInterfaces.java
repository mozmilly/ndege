package com.example.ndege.units.interfaces;


import com.example.ndege.units.models.MenuItems;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MenuItemInterfaces {
    @FormUrlEncoded
    @POST("unit/get_menu_items/")
    Call<List<MenuItems>> get_menu_items(
            @Field("id") int id
    );
 }

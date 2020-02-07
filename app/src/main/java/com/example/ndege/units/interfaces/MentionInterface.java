package com.example.ndege.units.interfaces;


import com.example.ndege.units.models.Mention;
import com.example.ndege.units.models.MenuItems;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MentionInterface {

    @GET("units/get_all_active_mentions/")
    Call<List<Mention>> get_all_active_mentions();

    @GET("units/get_discounted_items/")
    Call<List<MenuItems>> get_discounted_items();
}

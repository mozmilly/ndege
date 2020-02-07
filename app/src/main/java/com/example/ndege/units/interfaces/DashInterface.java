package com.example.ndege.units.interfaces;

import com.example.ndege.units.models.DashTiles;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DashInterface {
    @GET("units/get_dash/")
    Call<List<DashTiles>> get_dash_items();
}

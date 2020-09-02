package com.example.ndege.units.interfaces;

import com.example.ndege.units.models.Fee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeeInterface {

    @GET("unit/get_fees/")
    Call<List<Fee>> get_fees();
}

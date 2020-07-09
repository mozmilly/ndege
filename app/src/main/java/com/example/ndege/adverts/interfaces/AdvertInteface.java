package com.example.ndege.adverts.interfaces;

import com.example.ndege.adverts.models.Advert;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

public interface AdvertInteface {

    @GET("advert/get_ndege_adverts/")
    Call<List<Advert>> get_all_adverts();
}

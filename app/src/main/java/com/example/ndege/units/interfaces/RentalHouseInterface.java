package com.example.ndege.units.interfaces;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RentalHouseInterface {

    @FormUrlEncoded
    @POST("units/create_rental_house/")
    Call<Void> create_rental_house(
            @Field("unit_id") int unit_id,
            @Field("price") double price,
            @Field("location") String location,
            @Field("description") String description,
            @Field("number_of_bedrooms") int number_of_bedrooms
    );


    @Multipart
    @POST("units/add_portfolio/")
    Call<Void> create_portfolio(
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name,
            @Part("rental_house_id") RequestBody rental_house_id

    );

    @FormUrlEncoded
    @POST("units/book_rental_house/")
    Call<Void> book_rental_house(
            @Field("id") int id,
            @Field("username") String username
    );
}

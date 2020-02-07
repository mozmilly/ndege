package com.example.ndege.units.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InquiryInterface {

    @FormUrlEncoded
    @POST("advert/send_inquiry/")
    Call<Void> send_inquiry(
            @Field("sender") String sender,
            @Field("recipient") String recipient,
            @Field("title") String title,
            @Field("content") String content
    );
}

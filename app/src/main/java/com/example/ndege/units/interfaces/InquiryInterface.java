package com.example.ndege.units.interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InquiryInterface {

    @FormUrlEncoded
    @POST("advert/send_inquiry/")
    Call<Void> send_inquiry(
            @Field("sender") String sender,
            @Field("recipient") String recipient,
            @Field("title") String title,
            @Field("content") String content
    );

    @Multipart
    @POST("advert/send_enquiry/")
    Call<Void> send_enquiry(
            @Part MultipartBody.Part file,
            @Part("name")RequestBody name,
            @Part("sender") RequestBody sender,
            @Part("recipient") RequestBody recipient,
            @Part("title") RequestBody title,
            @Part("content") RequestBody content
    );
}

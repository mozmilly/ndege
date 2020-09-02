package com.example.ndege.login.interfaces;


import com.example.ndege.login.models.SignUp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SignUpInterface {
    @FormUrlEncoded
    @POST("accounts/api_sign_up/?format=json")
    Call<SignUp> sign_up(
            @Field("full_name") String full_name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("town") String town
    );
}

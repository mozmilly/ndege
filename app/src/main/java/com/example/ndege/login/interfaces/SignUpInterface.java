package com.example.ndege.login.interfaces;


import com.example.ndege.login.models.SignUp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SignUpInterface {
    @FormUrlEncoded
    @POST("sign_up/?format=json")
    Call<SignUp> sign_up(
            @Field("full_name") String full_name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password,
            @Field("email") String email,
            @Field("ref_code") String ref_code
    );
}

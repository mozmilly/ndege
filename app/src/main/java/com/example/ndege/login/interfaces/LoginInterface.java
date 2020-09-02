package com.example.ndege.login.interfaces;

import android.util.Log;

import com.example.ndege.login.models.Login;
import com.example.ndege.login.models.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {
    @FormUrlEncoded
    @POST("accounts/api_login/?format=json")
    Call<Login> perform_login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/switch_to_ndege_reseller/")
    Call<Login> switch_to_ndege_reseller(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("api-token-auth/")
    Call<Token> get_api_auth_token(
            @Field("username") String username,
            @Field("password") String password
    );
}

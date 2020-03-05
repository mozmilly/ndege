package com.example.ndege.tokens.interfaces;




import com.example.ndege.tokens.models.TokenModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenInterface {
    @FormUrlEncoded
    @POST("pooling/store_token/")
    Call<TokenModel> store_token(
            @Field("username") String username,
            @Field("token") String token,
            @Field("user_type") String user_type
    );
}

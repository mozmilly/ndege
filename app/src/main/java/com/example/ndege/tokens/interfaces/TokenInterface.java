package com.example.ndege.tokens.interfaces;




import com.example.ndege.tokens.models.TokenModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenInterface {
    @FormUrlEncoded
    @POST("accounts/update_firebase_token/")
    Call<Void> store_token(
            @Field("token") String token
    );
}

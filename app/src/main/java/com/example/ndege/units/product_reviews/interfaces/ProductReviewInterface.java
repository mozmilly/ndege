package com.example.ndege.units.product_reviews.interfaces;

import com.example.ndege.units.product_reviews.models.ProductRating;
import com.example.ndege.units.product_reviews.models.ProductReview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProductReviewInterface {

    @FormUrlEncoded
    @POST("units/get_product_reviews/")
    Call<List<ProductReview>> get_all_product_reviews(
          @Field("product_id") int product_id
    );

    @FormUrlEncoded
    @POST("units/make_a_product_review/")
    Call<Void> comment_on_product(
            @Field("username") String username,
            @Field("comment") String comment,
            @Field("menu_item_id") int menu_item_id,
            @Field("points") double points
    );

    @FormUrlEncoded
    @POST("units/get_product_rating/")
    Call<ProductRating> get_product_rating(
            @Field("menu_item_id") int menu_item_id
    );
}

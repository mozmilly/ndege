package com.example.ndege.units.interfaces;

import com.example.ndege.units.corecategories.models.CoreCategory;
import com.example.ndege.units.maincategories.models.MainCategory;
import com.example.ndege.units.menucategories.models.MenuCategory;
import com.example.ndege.units.models.ExtraField;
import com.example.ndege.units.models.ExtraPrice;
import com.example.ndege.units.models.LocationName;
import com.example.ndege.units.models.LocationPrice;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.PortfolioImage;
import com.example.ndege.units.models.Unit;
import com.example.ndege.units.subcorecategories.models.SubCoreCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UnitInterface {
    @FormUrlEncoded
    @POST("unit/get_units/")
    Call<List<Unit>> get_units(
            @Field("id") Integer id
    );

    @FormUrlEncoded
    @POST("unit/get_this_unit/")
    Call<Unit> get_unit(
            @Field("id") Integer id
    );


    @FormUrlEncoded
    @POST("unit/get_all_products/")
    Call<List<MenuItems>> get_all_menuitems(
            @Field("item_name") String item_name
    );

    @FormUrlEncoded
    @POST("unit/check_category_type/")
    Call<Void> check_type(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("unit/like_a_store/")
    Call<Void> like_a_store(
            @Field("customer_phone") String customer_phone,
            @Field("id") int id
    );

    @GET("unit/get_new_arrivals/")
    Call<List<MenuItems>> get_new_arrivals();

    @GET("unit/get_location_names/")
    Call<List<LocationName>> get_location_names();


    @FormUrlEncoded
    @POST("unit/compute_shipping_price/")
    Call<LocationPrice> compute_location_price(
            @Field("from_location") String from_location,
            @Field("to_location") String to_location
    );

    @FormUrlEncoded
    @POST("unit/get_menu_item_images/")
    Call<List<PortfolioImage>> get_menu_item_images(
            @Field("id") int id
    );
    //    Extra fields

    @FormUrlEncoded
    @POST("unit/get_extra_fields/")
    Call<List<ExtraField>> get_extra_fields(
            @Field("menu_item_id") int menu_item_id
    );

    @GET("unit/get_all_core_categories/")
    Call<List<CoreCategory>> get_all_core_categories();

    @FormUrlEncoded
    @POST("unit/get_sub_corecategories/")
    Call<List<SubCoreCategory>> get_sub_core_categories(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("unit/get_sub_corecategories_main_category/")
    Call<List<MainCategory>> get_this_main_categories(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("unit/get_main_category_menu_category/")
    Call<List<MenuCategory>> get_main_category_menu_category(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("unit/get_menu_items_api_view/")
    Call<List<MenuItems>> get_menu_items_api_view(
            @Field("id") int id,
            @Field("page") int page,
            @Field("app_id") String app_id
    );


    @FormUrlEncoded
    @POST("unit/get_all_menu_items_view/")
    Call<List<MenuItems>> get_all_menu_items(
            @Field("page") int page,
            @Field("app_id") String app_id
    );

    @FormUrlEncoded
    @POST("unit/get_core_cat_menu_items/")
    Call<List<MenuItems>> get_core_cat_menu_items(
            @Field("id") int id,
            @Field("page") int page,
            @Field("app_id") String app_id
    );

    @FormUrlEncoded
    @POST("unit/get_sub_core_cat_menu_items/")
    Call<List<MenuItems>> get_sub_core_cat_menu_items(
            @Field("id") int id,
            @Field("page") int page,
            @Field("app_id") String app_id
    );


    @FormUrlEncoded
    @POST("unit/get_main_cat_menu_items/")
    Call<List<MenuItems>> get_main_cat_menu_items(
            @Field("id") int id,
            @Field("page") int page,
            @Field("app_id") String app_id
    );


    @GET("unit/get_extra_prices/")
    Call<List<ExtraPrice>> get_extra_price();
}

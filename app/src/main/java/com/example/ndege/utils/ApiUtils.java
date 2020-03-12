package com.example.ndege.utils;


import com.example.ndege.adverts.interfaces.AdvertInteface;
import com.example.ndege.help.interfaces.HelpInterface;
import com.example.ndege.login.interfaces.LoginInterface;
import com.example.ndege.login.interfaces.SignUpInterface;
import com.example.ndege.tokens.interfaces.TokenInterface;
import com.example.ndege.units.interfaces.CheckOutInterface;
import com.example.ndege.units.interfaces.FeeInterface;
import com.example.ndege.units.interfaces.InquiryInterface;
import com.example.ndege.units.interfaces.MentionInterface;
import com.example.ndege.units.interfaces.OrderInterface;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.product_reviews.interfaces.ProductReviewInterface;

public class ApiUtils {
    private ApiUtils() {}
    public static final String BASE_URL = "http://ndege.pythonanywhere.com/";

    public static LoginInterface getLoginService(){
        return RetrofitClient.getClient(BASE_URL).create(LoginInterface.class);
    }

    public static SignUpInterface getSignUpService(){
        return RetrofitClient.getClient(BASE_URL).create(SignUpInterface.class);
    }

    public static UnitInterface getUnitService(){
        return RetrofitClient.getClient(BASE_URL).create(UnitInterface.class);
    }

    public static InquiryInterface getInquiryService(){
        return RetrofitClient.getClient(BASE_URL).create(InquiryInterface.class);
    }

    public static MentionInterface getMentionService(){
        return RetrofitClient.getClient(BASE_URL).create(MentionInterface.class);
    }

    public static FeeInterface getFeeService(){
        return RetrofitClient.getClient(BASE_URL).create(FeeInterface.class);
    }

    public static CheckOutInterface getCheckOutService(){
        return RetrofitClient.getClient(BASE_URL).create(CheckOutInterface.class);
    }

    public static OrderInterface getOrderService(){
        return RetrofitClient.getClient(BASE_URL).create(OrderInterface.class);
    }

    public static AdvertInteface get_advert_service(){
        return RetrofitClient.getClient(BASE_URL).create(AdvertInteface.class);
    }

    public static ProductReviewInterface get_product_review_service(){
        return RetrofitClient.getClient(BASE_URL).create(ProductReviewInterface.class);
    }

    public static TokenInterface getTokenService(){
        return RetrofitClient.getClient(BASE_URL).create(TokenInterface.class);
    }

    public static HelpInterface getHelpService(){
        return RetrofitClient.getClient(BASE_URL).create(HelpInterface.class);
    }
}

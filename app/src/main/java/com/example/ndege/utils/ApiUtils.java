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
    public static final String BASE_URL = "https://ndegeserver.pythonanywhere.com/";

    public static LoginInterface getLoginService(){
        return RetrofitClient.getClient(BASE_URL).create(LoginInterface.class);
    }

    public static SignUpInterface getSignUpService(){
        return RetrofitClient.getClient(BASE_URL).create(SignUpInterface.class);
    }

    public static UnitInterface getUnitService(String token){
        return RetrofitClient.createService(UnitInterface.class, token);
    }

    public static InquiryInterface getInquiryService(String token){
        return RetrofitClient.createService(InquiryInterface.class, token);
    }

    public static FeeInterface getFeeService(String token){
        return RetrofitClient.createService(FeeInterface.class, token);
    }

    public static CheckOutInterface getCheckOutService(String token){
        return RetrofitClient.createService(CheckOutInterface.class, token);
    }

    public static OrderInterface getOrderService(String token){
        return RetrofitClient.createService(OrderInterface.class, token);
    }

    public static AdvertInteface get_advert_service(String token){
        return RetrofitClient.createService(AdvertInteface.class, token);
    }

    public static ProductReviewInterface get_product_review_service(String token){
        return RetrofitClient.createService(ProductReviewInterface.class, token);
    }

    public static TokenInterface getTokenService(String token){
        return RetrofitClient.createService(TokenInterface.class, token);
    }

    public static HelpInterface getHelpService(String token){
        return RetrofitClient.createService(HelpInterface.class, token);
    }
}

package com.example.ndege;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ndege.login.LoginActivity;
import com.example.ndege.login.interfaces.LoginInterface;
import com.example.ndege.login.models.Login;
import com.example.ndege.login.models.Token;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences sp = getSharedPreferences("pref", 0);
                if (sp.getInt("PREFERENCES_LOGIN", 0)==1){

                    LoginInterface loginInterface = ApiUtils.getLoginService();
                    loginInterface.perform_login(sp.getString("user", "none"), "1234").enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if (response.code()==200){
                                SharedPreferences sp=getSharedPreferences("pref",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("is_ndege_reseller", response.body().isIs_ndege_reseller());
                                editor.putString("email", response.body().getEmail());
                                editor.putBoolean("selected_type", true);
                                editor.apply();
                                loginInterface.get_api_auth_token(sp.getString("user", "none"), "1234").enqueue(new Callback<Token>() {
                                    @Override
                                    public void onResponse(Call<Token> call, Response<Token> response) {
                                        if (response.code()==200){
                                            Token token = response.body();
                                            SharedPreferences sharedPreferences = getSharedPreferences("Prefs", MODE_PRIVATE);
                                            SharedPreferences.Editor   editor = sharedPreferences.edit();
                                            editor.putString("auth_token", "Token "+token.getToken());
                                            editor.putInt("is_loggedin", 1);
                                            editor.apply();
                                            Intent mainIntent = new Intent(SplashScreen.this, ViewCoreCategories.class);

                                            SplashScreen.this.startActivity(mainIntent);
                                            SplashScreen.this.finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Token> call, Throwable throwable) {

                                    }
                                });


                            } else {
                                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(intent);

                        }
                    });


                } else {
                    Intent intent = new Intent(SplashScreen.this, landing.class);
                    startActivity(intent);
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}

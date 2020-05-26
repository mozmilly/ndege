package com.example.ndege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Space;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.ndege.login.LoginActivity;
import com.example.ndege.login.interfaces.LoginInterface;
import com.example.ndege.login.models.Login;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.utils.ApiUtils;

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
                    loginInterface.perform_login(sp.getString("user", "none"), "").enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if (response.code()==200){
                                SharedPreferences sp=getSharedPreferences("pref",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("is_ndege_reseller", response.body().isIs_ndege_reseller());
                                editor.putBoolean("selected_type", true);
                                editor.apply();
                                Intent mainIntent = new Intent(SplashScreen.this, ViewCoreCategories.class);

                                SplashScreen.this.startActivity(mainIntent);
                                SplashScreen.this.finish();
                            } else {
                                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(SplashScreen.this, "Check your internet connection!!", Toast.LENGTH_SHORT).show();

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
            startActivity(intent);
        }
        return true;
    }
}

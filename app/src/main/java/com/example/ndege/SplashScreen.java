package com.example.ndege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Space;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ndege.units.corecategories.ViewCoreCategories;

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
                    Intent mainIntent = new Intent(SplashScreen.this, ViewCoreCategories.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
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

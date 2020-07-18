package com.example.ndege.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ndege.R;
import com.example.ndege.SplashScreen;
import com.example.ndege.login.interfaces.LoginInterface;
import com.example.ndege.login.models.Login;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.utils.ApiUtils;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NdegeTermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndege_terms);
        Button button = findViewById(R.id.accept_ndege);
        button.setOnClickListener(v -> {
            LoginInterface loginInterface = ApiUtils.getLoginService();
            loginInterface.switch_to_ndege_reseller(getSharedPreferences("pref", MODE_PRIVATE).getString("user", "none")).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.code()==200){
                        SharedPreferences sp=getSharedPreferences("pref",0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("is_ndege_reseller", response.body().isIs_ndege_reseller());
                        editor.putBoolean("selected_type", true);
                        editor.apply();
                        startActivity(new Intent(NdegeTermsActivity.this, SplashScreen.class));
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    recreate();
                }
            });
        });

    }
}

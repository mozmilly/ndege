package com.example.ndege.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ndege.R;
import com.example.ndege.landing;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    TextView username, email;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.username_id);
        email = findViewById(R.id.email_id);
        logout = findViewById(R.id.logout_btn);
        SharedPreferences sp = getSharedPreferences("pref", 0);
        SharedPreferences sp1 = getSharedPreferences("prefs", 0);
        username.setText(sp.getString("user", "none"));
        email.setText(sp.getString("email", "none"));
        logout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, landing.class);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            SharedPreferences.Editor editor1 = sp1.edit();
            editor1.clear();
            editor1.apply();
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
        });
    }
}

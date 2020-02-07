package com.example.ndege.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.ndege.R;
import com.example.ndege.login.interfaces.SignUpInterface;
import com.example.ndege.login.models.SignUp;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    SignUpInterface signUpInterface;

    private Spinner spinner;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        EditText fullname = findViewById(R.id.full_name_signup);
       // EditText username = findViewById(R.id.username_signup);
        //username.setVisibility(View.GONE);
       // EditText password = findViewById(R.id.password_signup);
        //EditText confirm_password = findViewById(R.id.confirm_password_signup);
       // password.setVisibility(View.GONE);
        //confirm_password.setVisibility(View.GONE);
        EditText email = findViewById(R.id.email_signup);
        EditText ref_code = findViewById(R.id.ref_code);

        Button signUp = findViewById(R.id.sign_up);
        TextView login = findViewById(R.id.sign_in);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpInterface = ApiUtils.getSignUpService();
                String full_name = fullname.getText().toString();
                String email1 = email.getText().toString();
                String refCode = ref_code.getText().toString();

                if (email1.length()>9){
                    signUpPost(full_name, getIntent().getStringExtra("pNumber"), "1234", "1234", email1, refCode);
                } else {
                    if (email.length()<4){
                        Toast.makeText(SignUpActivity.this, "Your email seems to be invalid!!", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signUpPost(String full_name, String username, String password, String confirmPassword, String email, String ref_code){
        signUpInterface.sign_up(full_name ,username, password, confirmPassword, email, ref_code).enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                Intent intent = new Intent(SignUpActivity.this, ViewCoreCategories.class);
                if (response.code()==300){
                    Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                } else {
                    Toast.makeText(SignUpActivity.this, "Phone number is already registered!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {

            }
        });
    }


}

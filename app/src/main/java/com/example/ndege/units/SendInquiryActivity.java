package com.example.ndege.units;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ndege.R;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.InquiryInterface;
import com.example.ndege.units.models.Unit;
import com.example.ndege.utils.ApiUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendInquiryActivity extends AppCompatActivity {
    EditText title, content;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_inquiry_service);

        title = findViewById(R.id.inquiry_title_id);
        content = findViewById(R.id.inquiry_content_id);

        submit = findViewById(R.id.inquiry_submit);


        title.setText("Inquiry about ");
        if (Objects.requireNonNull(getIntent().getStringExtra("name")).equalsIgnoreCase("item")){
            content.setText(getIntent().getStringExtra("content"));
        } else if (getIntent().getStringExtra("name").equalsIgnoreCase("response")){
            title.setText("Reply to "+getIntent().getStringExtra("title"));
            content.setText("");
        } else {
            content.setText("");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences("pref", 0);
                String username = sp.getString("user", "no user");
                InquiryInterface inquiryInterface = ApiUtils.getInquiryService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));

                if (Objects.requireNonNull(getIntent().getStringExtra("name")).equalsIgnoreCase("response")){
                    inquiryInterface.send_inquiry(username, getIntent().getStringExtra("recipient"), title.getText().toString(),
                            content.getText().toString()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code()==200){
                                Toast.makeText(SendInquiryActivity.this, "Message sent successfully",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SendInquiryActivity.this, ViewCoreCategories.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(SendInquiryActivity.this,
                                        String.valueOf(response.code()), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {


                        }
                    });
                } else {
                    inquiryInterface.send_inquiry(username, "0753540580", title.getText().toString(),
                            content.getText().toString()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code()==200){
                                Toast.makeText(SendInquiryActivity.this, "Message sent successfully",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SendInquiryActivity.this, ViewCoreCategories.class);
                                startActivity(intent);

                            } else {


                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {

                        }
                    });

                }


            }
        });

    }
}

package com.example.ndege.units;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ndege.R;

public class ClientDetailsActivity extends AppCompatActivity {
    EditText clientPhone, clientName, margin;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        clientName = findViewById(R.id.client_name);
        clientPhone = findViewById(R.id.client_phone);
        margin = findViewById(R.id.my_margin);
        next = findViewById(R.id.next_btn);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (margin.getText().toString().isEmpty()){
                    Toast.makeText(ClientDetailsActivity.this, "You must fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ClientDetailsActivity.this, CheckoutActivity.class);
                    intent.putExtra("client_name", clientName.getText().toString());
                    intent.putExtra("client_phone", clientPhone.getText().toString());
                    intent.putExtra("margin", margin.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }
}

package com.example.ndege.units.orders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.product_reviews.interfaces.ProductReviewInterface;
import com.example.ndege.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentOnProduct extends AppCompatActivity {
    EditText comment;
    Button commentBtn;
    RatingBar ratingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_on_product);

        comment = findViewById(R.id.your_comment);
        commentBtn = findViewById(R.id.comment);
        ratingbar= findViewById(R.id.ratingBar);
        int product_id = getIntent().getIntExtra("product_id", 0);
        SharedPreferences sp = getSharedPreferences("pref", 0);
        String username = sp.getString("user", "no user");

        ProductReviewInterface productReviewInterface = ApiUtils.get_product_review_service();

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommentOnProduct.this, String.valueOf(ratingbar.getRating()), Toast.LENGTH_SHORT).show();
                productReviewInterface.comment_on_product(username, comment.getText().toString(), product_id, ratingbar.getRating()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code()==200){
                            Intent intent = new Intent(CommentOnProduct.this, ViewLargerImageActivity.class);
                            intent.putExtra("image", getIntent().getStringExtra("image"));
                            intent.putExtra("menu_item", "true");
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });



    }
}

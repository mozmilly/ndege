package com.example.ndege.adverts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.ndege.R;
import com.example.ndege.adverts.models.Advert;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.squareup.picasso.Picasso;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Advert advert = ViewCoreCategories.getAdvert();



        ImageView imageView = findViewById(R.id.message_image);

        if (advert.getMy_image()!=null){
            if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+advert.getMy_image())){
                Picasso.with(MessageActivity.this)
                        .load("https://bombaservices.pythonanywhere.com"+advert.getMy_image())
                        .placeholder(R.drawable.place_holder)
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);
            }
        }
        TextView title = findViewById(R.id.advert_title);
        title.setText(advert.getTitle());
        TextView long_des = findViewById(R.id.advert_description_long);
        long_des.setText(advert.getLong_description());


    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

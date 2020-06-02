package com.example.ndege.help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ndege.R;
import com.example.ndege.help.interfaces.HelpInterface;
import com.example.ndege.help.models.Config;
import com.example.ndege.help.models.Help;
import com.example.ndege.help.models.HelpAdapter;
import com.example.ndege.units.orders.ViewOrderDetailsActivity;
import com.example.ndege.utils.ApiUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends YouTubeBaseActivity implements HelpAdapter.OnItemClicked {
    RecyclerView recyclerView;
    HelpAdapter helpAdapter;
    List<Help> helpList = new ArrayList<>();
    private static Help help;

    public static Help getHelp() {
        return help;
    }

    public static void setHelp(Help help) {
        HelpActivity.help = help;
    }
    Button call, whatsapp;
    private static final int REQUEST_PHONE_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        call = findViewById(R.id.call_support);
        whatsapp = findViewById(R.id.whatsapp);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "0753540580"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        if (ContextCompat.checkSelfPermission(HelpActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(HelpActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                        }
                        else
                        {
                            startActivity(intent);

                        }

                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);

                }
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String url = "https://wa.me/254753540580";
                intentWhatsapp.setData(Uri.parse(url));
                intentWhatsapp.setPackage("com.whatsapp");
                startActivity(intentWhatsapp);

            }
        });
        recyclerView = findViewById(R.id.help_recycler);
        HelpInterface helpInterface = ApiUtils.getHelpService();

        helpInterface.get_all_help().enqueue(new Callback<List<Help>>() {
            @Override
            public void onResponse(Call<List<Help>> call, Response<List<Help>> response) {
                if (response.code()==200){
                    helpList = response.body();
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    helpAdapter = new HelpAdapter(helpList, HelpActivity.this);
                    recyclerView.setAdapter(helpAdapter);
                    helpAdapter.setOnClick(HelpActivity.this);
                    helpAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Help>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ViewHelpDetails.class);
        setHelp(helpList.get(position));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else
                {

                }
                return;
            }
        }
    }
}

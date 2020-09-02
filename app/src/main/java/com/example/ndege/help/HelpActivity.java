package com.example.ndege.help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ndege.R;
import com.example.ndege.help.interfaces.HelpInterface;
import com.example.ndege.help.models.Config;
import com.example.ndege.help.models.Help;
import com.example.ndege.help.models.HelpAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        recyclerView = findViewById(R.id.help_recycler);
        HelpInterface helpInterface = ApiUtils.getHelpService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));

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
}

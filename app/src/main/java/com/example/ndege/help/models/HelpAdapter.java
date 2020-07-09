package com.example.ndege.help.models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;
import com.example.ndege.help.HelpActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder>{
    List<Help> helpList;
    Context context;

    View this_view;

    private static final int RECOVERY_REQUEST = 1;


    private YouTubePlayer player;

    public HelpAdapter(List<Help> helpList, Context context) {
        this.helpList = helpList;
        this.context = context;
    }

    //declare interface
    private OnItemClicked onClick;


    public interface OnItemClicked {
        void onItemClick(int position);
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(helpList.get(position).getTitle());
        holder.content.setText(helpList.get(position).getContent());

        holder.imageView.setVisibility(View.VISIBLE);
        holder.playBtn.setVisibility(View.VISIBLE);

        if (helpList.get(position).getFile_image()!=null){
            if (URLUtil.isValidUrl("https://bombaservices.pythonanywhere.com"+helpList.get(position).getFile_image())){
                Picasso.with(context)
                        .load("https://bombaservices.pythonanywhere.com"+helpList.get(position).getFile_image())
                        .placeholder(R.drawable.place_holder)
                        .into(holder.imageView);
            }
        }

        holder.video.setVisibility(View.GONE);

        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YouTubePlayerView youTubePlayerView;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_player_pop_up, viewGroup, false);
                youTubePlayerView = dialogView.findViewById(R.id.youtube_player1);
                if (player!=null){
                    player.release();
                }
                youTubePlayerView.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        player = youTubePlayer;
                        if (!b) {
                            youTubePlayer.cueVideo(helpList.get(position).getVideo()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                        } else {
                            youTubePlayer.cueVideo(helpList.get(position).getVideo()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo

                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        if (youTubeInitializationResult.isUserRecoverableError()) {
                            youTubeInitializationResult.getErrorDialog(((HelpActivity) context), RECOVERY_REQUEST).show();
                        } else {
                            Toast.makeText(context, "This Error", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setCancelable(true);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        alertDialog.cancel();
                    }
                });
                Button button = dialogView.findViewById(R.id.button_close);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                    }
                });
                alertDialog.show();
            }
        });

        holder.readMore.setOnClickListener(v -> onClick.onItemClick(position));

    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, content, readMore;
        public YouTubePlayerView video;
        public ImageView imageView, playBtn;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            title = view.findViewById(R.id.help_title);
            content = view.findViewById(R.id.help_content);
            video = view.findViewById(R.id.youtube_player);
            parent = view.findViewById(R.id.help_parent);
            imageView = view.findViewById(R.id.imageViewItem);
            playBtn = view.findViewById(R.id.btnPlay);
            readMore = view.findViewById(R.id.read_more_text);
        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

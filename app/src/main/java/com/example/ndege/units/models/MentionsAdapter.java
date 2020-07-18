package com.example.ndege.units.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ndege.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MentionsAdapter extends RecyclerView.Adapter<MentionsAdapter.MyViewHolder>{
    List<Mention> mentionList;
    Context context;

    //declare interface
    private OnMentionItemClicked onClick;

    public interface OnMentionItemClicked {
        void onMentionItemClick(int position);
    }

    public MentionsAdapter(List<Mention> mentionList, Context context) {
        this.mentionList = mentionList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description;
        public ImageView image;
        Button more;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            title = view.findViewById(R.id.mention_title);
            description = view.findViewById(R.id.mention_description);
            image = view.findViewById(R.id.mention_id_image);
            more = view.findViewById(R.id.read_more_btn);
            parent = view.findViewById(R.id.mention_parent_id);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mention_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (mentionList.get(i).getImage()!=null){
            if (URLUtil.isValidUrl("https://storage.googleapis.com/ndege/"+mentionList.get(i).getImage())){
                Picasso.with(context)
                        .load("https://storage.googleapis.com/ndege/"+mentionList.get(i).getImage())
                        .placeholder(R.drawable.place_holder)
                        .into(myViewHolder.image);
                myViewHolder.image.setVisibility(View.VISIBLE);
            }
        }

        myViewHolder.title.setText(mentionList.get(i).getTitle());
        myViewHolder.description.setText(mentionList.get(i).getDescription());

        myViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onMentionItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mentionList.size();
    }

    public void setOnClick(OnMentionItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

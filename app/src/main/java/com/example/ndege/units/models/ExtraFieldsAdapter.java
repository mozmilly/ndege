package com.example.ndege.units.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;

import java.util.List;

public class ExtraFieldsAdapter extends RecyclerView.Adapter<ExtraFieldsAdapter.MyViewHolder>{

    List<ExtraField> extraFieldList;

    Context context;

    public ExtraFieldsAdapter(List<ExtraField> extraFieldList, Context context) {
        this.extraFieldList = extraFieldList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.extra_field_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText((extraFieldList.get(position).getName()+":"));
        holder.value.setText(extraFieldList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return extraFieldList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, value;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.tv_name);
            value = view.findViewById(R.id.tv_value);

            parent = view.findViewById(R.id.tv_parent);
        }

    }

}

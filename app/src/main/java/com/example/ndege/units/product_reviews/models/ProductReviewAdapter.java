package com.example.ndege.units.product_reviews.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;

import java.util.List;

public class ProductReviewAdapter extends  RecyclerView.Adapter<ProductReviewAdapter.MyViewHolder>{

    List<ProductReview> productReviewList;
    Context context;

    private boolean isLoaderVisible = false;

    public ProductReviewAdapter(List<ProductReview> productReviewList, Context context) {
        this.productReviewList = productReviewList;
        this.context = context;
    }

    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText((productReviewList.get(position).getUser().getFirst_name()));
        holder.comment.setText((productReviewList.get(position).getComment()));
        holder.rating.setText(String.valueOf(productReviewList.get(position).getPoints()));
        holder.ratingBar.setRating(Float.parseFloat(String.valueOf(productReviewList.get(position).getPoints())));

    }

    @Override
    public int getItemCount() {
        return productReviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, comment, rating;
        RatingBar ratingBar;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.product_review_user_name);
            comment = view.findViewById(R.id.product_review_comment);
            rating = view.findViewById(R.id.rating_id);
            ratingBar = view.findViewById(R.id.ratingBarDisplay);

        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateList(List<ProductReview> list){
        productReviewList = list;
        notifyDataSetChanged();
    }

    public void addItems(List<ProductReview> postItems) {
        productReviewList.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;

    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = productReviewList.size() - 1;
        ProductReview item = getItem(position);
        if (item != null) {
            productReviewList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        productReviewList.clear();
        notifyDataSetChanged();
    }

    ProductReview getItem(int position) {
        return productReviewList.get(position);
    }

}
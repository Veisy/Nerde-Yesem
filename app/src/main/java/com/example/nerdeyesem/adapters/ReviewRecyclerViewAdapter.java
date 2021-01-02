package com.example.nerdeyesem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nerdeyesem.databinding.ReviewsRecyclerviewItemBinding;
import com.example.nerdeyesem.model.ReviewsModel;

import static android.content.ContentValues.TAG;

// RecyclerView Adapter to fill Reviews RecyclerView
public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewsViewHolder> {

    private final Context mContext;
    private final ReviewsModel reviewsModel;

    public ReviewRecyclerViewAdapter(Context context, ReviewsModel reviewsModel) {
        this.mContext = context;
        this.reviewsModel = reviewsModel;
    }

    public static class ReviewsViewHolder extends RecyclerView.ViewHolder {
        ReviewsRecyclerviewItemBinding binding;


        public ReviewsViewHolder(@NonNull ReviewsRecyclerviewItemBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewsViewHolder(ReviewsRecyclerviewItemBinding
                .inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        ReviewsModel.ChildReview childReview = reviewsModel.getReviews()
                .get(position).getChildReview();

        String reviewRatingColor = "#" + childReview.getRatingColor();
        int color = Color.parseColor(reviewRatingColor);

        try {
            holder.binding.viewReviewColorRatingBar
                    .setBackgroundColor(color);
            holder.binding.textViewReviewRatingText
                    .setTextColor(color);
            holder.binding.textViewReviewRatingText
                    .setText(childReview.getRatingText());
            holder.binding.ratingBarReviewRating
                    .setRating(childReview.getRating().floatValue());
            holder.binding.textViewReviewTime
                    .setText(childReview.getReviewTime());
            holder.binding.textViewReviewText.setText(childReview.getReviewText());
        } catch (Exception e) {
            Toast.makeText(mContext, "Error occurred while extracting review data from Zomato"
                    , Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onBindViewHolder: Error while settings views");
        }

    }

    @Override
    public int getItemCount() {
        return reviewsModel.getReviews().size();
    }



}

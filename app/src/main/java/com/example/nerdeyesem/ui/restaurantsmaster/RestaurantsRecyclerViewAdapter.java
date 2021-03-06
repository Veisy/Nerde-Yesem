package com.example.nerdeyesem.ui.restaurantsmaster;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nerdeyesem.R;
import com.example.nerdeyesem.databinding.RestaurantRecyclerviewItemBinding;

import static android.content.ContentValues.TAG;

// This recycler lists the data it retrieved from the RestaurantViewModel
// via ZomatoRestaurantsRepository in the card view design.
// As in other parts of the project, View Binding is used here.

// The accepted best way was followed when adding click listeners to the recyclerview elements.
// We would have to create a separate click listener object for each recyclerview element,
// and this is very bad practice for large lists, as warned in Android Codelab.
// We connected all elements to a single click listener object with this scheme
// using the interface we have defined here.
// For more info: https://www.youtube.com/watch?v=69C1ljfDvl0&ab_channel=CodingWithMitch
public class RestaurantsRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantsRecyclerViewAdapter.RestaurantsViewHolder> {

    private final Context mContext;
    private final RestaurantsModel restaurantsModel;
    private final OnRestaurantClickListener mOnRestaurantClickListener;

    public RestaurantsRecyclerViewAdapter(Context mContext, RestaurantsModel restaurantsModel,
                                          OnRestaurantClickListener onRestaurantClickListener) {
        this.mContext = mContext;
        this.restaurantsModel = restaurantsModel;
        this.mOnRestaurantClickListener = onRestaurantClickListener;
    }

    public static class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RestaurantRecyclerviewItemBinding binding;
        OnRestaurantClickListener onRestaurantClickListener;

        public RestaurantsViewHolder(@NonNull RestaurantRecyclerviewItemBinding b,
                                     OnRestaurantClickListener onRestaurantClickListener) {
            super(b.getRoot());
            binding = b;
            this.onRestaurantClickListener = onRestaurantClickListener;
            binding.cardViewRestaurant.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onRestaurantClickListener.onRestaurantClick(position, binding.cardViewRestaurant);
        }
    }

    public interface OnRestaurantClickListener {
        void onRestaurantClick(int position, View view);
    }

    @NonNull
    @Override
    public RestaurantsRecyclerViewAdapter.RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantsViewHolder(RestaurantRecyclerviewItemBinding
                .inflate(LayoutInflater.from(mContext), parent, false)
                , mOnRestaurantClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsRecyclerViewAdapter.RestaurantsViewHolder holder, int position) {
        RestaurantsModel.RestaurantModel restaurantModel =
                restaurantsModel.getSingleRestaurantModelList().get(position).getRestaurantModel();

        try {
            //Fields, some of them formatted
            String restaurantRatingColor = "#" + restaurantModel.getRestaurantRating().getRatingColor();
            String restaurantVotesString = "(" + restaurantModel.getRestaurantRating().getVotes() +" votes)";
            String restaurantHighlights = restaurantModel.getHighlights().toString()
                    .replace("[", "").replace("]", "");

            //Get values from API via our MVVM architecture and pass to views.
            holder.binding.textViewRestaurantName
                    .setText(restaurantModel.getName());
            holder.binding.textViewRestaurantAddress
                    .setText(restaurantModel.getRestaurantAddress().getAddress());
            holder.binding.ratingBarRating
                    .setRating(restaurantModel.getRestaurantRating().getAggregateRating().floatValue());
            holder.binding.textViewRating
                    .setText(restaurantModel.getRestaurantRating().getRatingText());
            holder.binding.textViewRestaurantCuisines
                    .setText(restaurantModel.getCuisines());
            holder.binding.textViewRestaurantHighlights.setText(restaurantHighlights);
            holder.binding.textViewVotes.setText(restaurantVotesString);

            holder.binding.textViewRating
                    .setTextColor(Color.parseColor(restaurantRatingColor));

            boolean isFeaturedImageExist = (restaurantModel.getFeaturedImage() != null
                    && !restaurantModel.getFeaturedImage().equals(""));
            boolean isRatingColorDefault = (restaurantRatingColor.equals("#CBCBCB")
                    || restaurantModel.getRestaurantRating().getRatingText().equals("0"));

            //Load featured image if exist
            if (isFeaturedImageExist)
                Glide.with(mContext).load(restaurantModel.getFeaturedImage())
                        .into(holder.binding.imageViewRestaurantImage);

            // If returned rating color is default grey color,
            // we change it to our bluish color in restaurantName and colorRatingBar.
            // Else take returned color.
            int color = isRatingColorDefault
                    ? ContextCompat.getColor(mContext, R.color.colorBlue_700)
                    : Color.parseColor(restaurantRatingColor);

            holder.binding.viewColorRatingBar.setBackgroundColor(color);
            holder.binding.textViewRestaurantName.setTextColor(color);

        } catch (Exception e) {
            Toast.makeText(mContext, "Error occurred while extracting data from Zomato"
                    , Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onBindViewHolder: Error while settings views");
        }
    }

    @Override
    public int getItemCount() {
        return restaurantsModel.getSingleRestaurantModelList().size();
    }

}

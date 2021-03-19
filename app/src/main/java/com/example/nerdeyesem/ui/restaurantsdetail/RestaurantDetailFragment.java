package com.example.nerdeyesem.ui.restaurantsdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.nerdeyesem.R;
import com.example.nerdeyesem.databinding.FragmentRestaurantDetailBinding;
import com.example.nerdeyesem.ui.restaurantsmaster.RestaurantsModel;
import com.example.nerdeyesem.utils.Resource;
import com.example.nerdeyesem.ui.restaurantsmaster.RestaurantsViewModel;
import com.example.nerdeyesem.ui.login.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RestaurantDetailFragment extends Fragment {
    private FragmentRestaurantDetailBinding binding;

    private int position;

    private RestaurantsViewModel restaurantsViewModel;
    private ReviewsViewModel reviewsViewModel;

    private RestaurantsModel.RestaurantModel restaurantModel;
    private ReviewsModel reviewsModel;

    private UserViewModel userViewModel;
    private NavController navController;

    private String restaurantRatingColor;
    private String restaurantHighlights;
    private String restaurantVotesString;
    private String restaurantTimings;
    private int color;

    public RestaurantDetailFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantDetailBinding
                .inflate(inflater, container, false);
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(getString(R.string.restaurant_details));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            position = arguments.getInt("position", 0);

            initRestaurantViewModel();
            getRestaurantModel();
            initReviewViewModel();
            initReviewLiveDataObserver();
        }

        initUserViewModel();
        initNavigationController(view);

        // Observe changes on LiveData<FirebaseUser> observable object.
        // If there is no user currently logged in, redirect to the login page
        initUserLiveDataObserver();

        // Get values from API via our MVVM architecture,
        // format if needed, then pass to views.
        formatStringFields();
        setColorAndImage();
        setFields();
    }

    private void initRestaurantViewModel() {
        restaurantsViewModel = new ViewModelProvider(requireActivity())
                .get(RestaurantsViewModel.class);
    }

    private void getRestaurantModel() {
        // Get RestaurantModel from RestaurantsViewModel class if not null.
        if (restaurantsViewModel.getRestaurants().getValue() != null)
            restaurantModel = Objects.requireNonNull(restaurantsViewModel
                    .getRestaurants().getValue().data)
                    .getSingleRestaurantModelList().get(position).getRestaurantModel();
    }

    private void initReviewViewModel() {
        reviewsViewModel = new ViewModelProvider(requireActivity())
                .get(ReviewsViewModel.class);
        int resId = Integer.parseInt(restaurantModel.getId());
        reviewsViewModel.findReviews(resId);
    }

    private void initReviewLiveDataObserver() {
        reviewsViewModel.getReviews().removeObservers(getViewLifecycleOwner());
        reviewsViewModel.getReviews().observe(getViewLifecycleOwner(),
                reviewsModelResource -> {
                    if (reviewsModelResource.status == Resource.Status.SUCCESS) {
                        reviewsModel = reviewsModelResource.data;

                        setReviewsRecyclerView();
                    } else {
                       binding.textViewReviewsStatus.setVisibility(View.VISIBLE);
                       binding.textViewReviewsStatus.setText(reviewsModelResource.message);
                    }
                });
    }


    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    private void initNavigationController(View view) {
        navController = Navigation.findNavController(view);
    }

    // If user auth failed, redirect to login screen.
    private void initUserLiveDataObserver() {
        userViewModel.getUser().removeObservers(getViewLifecycleOwner());
        userViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUserResource -> {
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            if ( !(firebaseUserResource.status == Resource.Status.SUCCESS
                    && firebaseUserResource.data != null) ) {
                toolbar.setVisibility(View.GONE);
                navController.navigate(R.id.loginFragment);
            }
        });
    }

    private void formatStringFields() {
        restaurantRatingColor = "#" + restaurantModel.getRestaurantRating().getRatingColor();

        restaurantVotesString = "(" + restaurantModel.getRestaurantRating().getVotes() +" votes)";

        restaurantHighlights = restaurantModel.getHighlights().toString()
                .replace("[", "").replace("]", "");

        restaurantTimings = restaurantModel.getTimings()
                .replaceAll("," , "\n");
    }

    private void setColorAndImage() {
        boolean isFeaturedImageExist = (restaurantModel.getFeaturedImage() != null
                && !restaurantModel.getFeaturedImage().equals(""));
        boolean isRatingColorDefault = (restaurantRatingColor.equals("#CBCBCB")
                || restaurantModel.getRestaurantRating().getRatingText().equals("0"));

        // If returned rating color is default grey color,
        // we change it to our bluish color in restaurantName and colorRatingBar.
        // Else take returned color.
        color = isRatingColorDefault
                ? ContextCompat.getColor(requireContext(), R.color.colorBlue_700)
                : Color.parseColor(restaurantRatingColor);

        //Load featured image if exist
        if (isFeaturedImageExist)
            Glide.with(requireContext()).load(restaurantModel.getFeaturedImage()).centerCrop()
                    .into(binding.imageViewDetailRestaurantImage);
    }

    private void setFields() {
        binding.textViewDetailRestaurantName
                .setText(restaurantModel.getName());
        binding.textViewDetailRestaurantAddress
                .setText(restaurantModel.getRestaurantAddress().getAddress());
        binding.ratingBarDetailRestaurantRating
                .setRating(restaurantModel.getRestaurantRating().getAggregateRating().floatValue());
        binding.textViewDetailRestaurantRating
                .setText(restaurantModel.getRestaurantRating().getRatingText());
        binding.textViewDetailRestaurantCuisines
                .setText(restaurantModel.getCuisines());
        binding.textViewDetailRestaurantHighlights
                .setText(restaurantHighlights);
        binding.textViewDetailRestaurantVotes
                .setText(restaurantVotesString);
        binding.textViewDetailRestaurantTimings
                .setText(restaurantTimings);
        binding.textViewDetailRestaurantName
                .setTextColor(color);
        binding.viewDetailColorRatingBar
                .setBackgroundColor(color);
        binding.textViewDetailRestaurantRating
                .setTextColor(Color.parseColor(restaurantRatingColor));
    }

    private void setReviewsRecyclerView() {
        ReviewRecyclerViewAdapter reviewRecyclerViewAdapter =
                new ReviewRecyclerViewAdapter(requireContext(), reviewsModel);
        binding.recyclerViewReviews.setAdapter(reviewRecyclerViewAdapter);
        binding.recyclerViewReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        LayoutAnimationController layoutAnimationController = AnimationUtils
                .loadLayoutAnimation(requireContext(), R.anim.recyclerview_layout_animation);
        binding.recyclerViewReviews.setLayoutAnimation(layoutAnimationController);
    }


}
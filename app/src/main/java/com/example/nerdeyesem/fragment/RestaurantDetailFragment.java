package com.example.nerdeyesem.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.nerdeyesem.R;
import com.example.nerdeyesem.databinding.FragmentRestaurantDetailBinding;
import com.example.nerdeyesem.model.RestaurantsModel;
import com.example.nerdeyesem.utils.Resource;
import com.example.nerdeyesem.viewmodel.RestaurantsViewModel;
import com.example.nerdeyesem.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RestaurantDetailFragment extends Fragment {
    private FragmentRestaurantDetailBinding binding;
    private RestaurantsModel.RestaurantModel restaurantModel;

    private UserViewModel userViewModel;
    private NavController navController;

    private String restaurantRatingColor;
    private String restaurantHighlights;
    private String restaurantVotesString;
    private String restaurantTimings;
    private int color;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            int position = arguments.getInt("position", 0);
            RestaurantsViewModel restaurantsViewModel =
                    new ViewModelProvider(requireActivity()).get(RestaurantsViewModel.class);
            if (restaurantsViewModel.getRestaurants().getValue() != null)
                restaurantModel = Objects.requireNonNull(restaurantsViewModel
                        .getRestaurants().getValue().data)
                        .getSingleRestaurantModelList().get(position).getRestaurantModel();
        }
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

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    private void initNavigationController(View view) {
        navController = Navigation.findNavController(view);
    }

    // If user auth failed, redirect to login screen.
    private void initUserLiveDataObserver() {
        userViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUserResource -> {
            if ( !(firebaseUserResource.status == Resource.Status.SUCCESS
                    && firebaseUserResource.data != null) ) {
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
}
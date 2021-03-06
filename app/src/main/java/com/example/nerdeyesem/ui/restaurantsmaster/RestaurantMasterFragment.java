package com.example.nerdeyesem.ui.restaurantsmaster;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nerdeyesem.R;
import com.example.nerdeyesem.databinding.FragmentRestaurantMasterBinding;
import com.example.nerdeyesem.location.GpsUtils;
import com.example.nerdeyesem.location.NewLocationChecker;
import com.example.nerdeyesem.utils.Resource;
import com.example.nerdeyesem.location.LocationViewModel;
import com.example.nerdeyesem.ui.login.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RestaurantMasterFragment extends Fragment implements RestaurantsRecyclerViewAdapter.OnRestaurantClickListener {
    private FragmentRestaurantMasterBinding binding;
    private UserViewModel userViewModel;
    private NavController navController;

    private LocationViewModel locationViewModel;

    // Reference to the return value of registerForActivityResult(),
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private RestaurantsViewModel restaurantsViewModel;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setToolbar();

        binding = FragmentRestaurantMasterBinding
                .inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void setToolbar() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.nearby_restaurants));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUserViewModel();
        initNavigationController(view);

        // Observe changes on LiveData<FirebaseUser> observable object.
        // If there is no user currently logged in, redirect to the login page
        initUserLiveDataObserver();

        initRestaurantViewModel();
        initRestaurantsLiveDataObserver();

        initLocationViewModel();

        //Register callback that get the permission request.
        initRegisterPermissionCallback();
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    private void initNavigationController(View view) {
        navController = Navigation.findNavController(view);
    }

    private void initUserLiveDataObserver() {
        userViewModel.getUser().removeObservers(getViewLifecycleOwner());
        userViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUserResource -> {
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            if (firebaseUserResource.status == Resource.Status.SUCCESS
                    && firebaseUserResource.data != null) {
                //Make layout visible.
                toolbar.setVisibility(View.VISIBLE);
                binding.constraintLayoutFragmentRestaurantMaster.setVisibility(View.VISIBLE);
                binding.progressBarRecyclerViewStatus.setVisibility(View.VISIBLE);

                //Check location permission, and get location if permission granted.
                getLocation();
            } else {
                toolbar.setVisibility(View.GONE);
                navController.navigate(R.id.loginFragment);
            }
        });
    }

    private void initRestaurantViewModel() {
        restaurantsViewModel = new ViewModelProvider(requireActivity())
                .get(RestaurantsViewModel.class);
    }

    private void initRestaurantsLiveDataObserver() {
        restaurantsViewModel.getRestaurants().removeObservers(getViewLifecycleOwner());
        restaurantsViewModel.getRestaurants().observe(getViewLifecycleOwner(),
                listResource -> {
                    if (listResource.status == Resource.Status.SUCCESS) {
                        setRestaurantRecyclerView(listResource.data);
                    } else if (listResource.status == Resource.Status.LOADING) {
                        binding.progressBarRecyclerViewStatus.setVisibility(View.VISIBLE);
                    } else {
                        binding.progressBarRecyclerViewStatus.setVisibility(View.GONE);
                        binding.textViewStatusInformation.setText(listResource.message);
                    }
                });

    }

    private void setRestaurantRecyclerView(RestaurantsModel restaurantsModel) {
        binding.progressBarRecyclerViewStatus.setVisibility(View.GONE);
        //binding.recyclerViewRestaurants.setHasFixedSize(true);
        RestaurantsRecyclerViewAdapter restaurantsRecyclerViewAdapter =
                new RestaurantsRecyclerViewAdapter(requireContext(),
                        restaurantsModel, this);
        binding.recyclerViewRestaurants
                .setAdapter(restaurantsRecyclerViewAdapter);
        binding.recyclerViewRestaurants
                .setLayoutManager(new LinearLayoutManager(requireContext()));
        LayoutAnimationController layoutAnimationController = AnimationUtils
                .loadLayoutAnimation(requireContext(), R.anim.recyclerview_layout_animation);
        binding.recyclerViewRestaurants.setLayoutAnimation(layoutAnimationController);
    }

    private void initLocationViewModel() {
       locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
    }

    private void initRegisterPermissionCallback() {
        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog.
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                    if (isGranted) {
                        //We check permission anyway
                        //because Android says don't assume any system behaviour,
                        //and check permission every time.
                        getLocation();
                    } else {
                        binding.textViewStatusInformation
                                .setText(R.string.location_permission_denied_message);
                        binding.progressBarRecyclerViewStatus.setVisibility(View.GONE);
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            //After obtaining the location permit, we can get the location information.
            //Check if GPS is enabled, and prompt the user to change location settings if needed.
            GpsUtils.checkIfGPSEnabled(requireActivity());
            observeLocationUpdate();
        } else {
            requestPermission();
        }
    }

    private void observeLocationUpdate() {
        // Check is there any active observers, if not create one.
        // There is a danger of memory leak by creating a new observer object here every time here.
        locationViewModel.getLocationLiveData().removeObservers(getViewLifecycleOwner());
        locationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(),
                locationModel -> {
                    if (locationModel != null) {
                        binding.textViewStatusInformation.setVisibility(View.GONE);

                        if(NewLocationChecker.isNewLocationNeeded(requireContext(),
                                NewLocationChecker.previousLocation ,locationModel)) {
                            //Get restaurants.
                            restaurantsUpdate(locationModel.getLatitude(),
                                    locationModel.getLongitude());
                            NewLocationChecker.previousLocation = locationModel;
                        } else {
                            setRestaurantRecyclerView(Objects.requireNonNull(restaurantsViewModel
                                    .getRestaurants().getValue()).data);
                        }
                    }
                });

        locationViewModel.getIsGPSEnable().removeObservers(getViewLifecycleOwner());
        locationViewModel.getIsGPSEnable().observe(getViewLifecycleOwner(),
                aBoolean -> {
                    if (!aBoolean) {
                        binding.textViewStatusInformation
                                .setText(R.string.location_gps_disabled_message);
                        binding.progressBarRecyclerViewStatus.setVisibility(View.GONE);
                    } else {
                        binding.textViewStatusInformation
                                .setText(R.string.location_gps_enabled);
                    }
                });

    }

    private void requestPermission() {
        //Runtime permissions supported API level 23 or higher.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Inform the user why location permission is needed.
            showLocationPermissionDialog();
        }
    }

    private void showLocationPermissionDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.location_permission_title);
        builder.setMessage(R.string.location_permission_inform_message);

        builder.setPositiveButton(R.string.okay, (dialog, which) ->
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION));

        builder.setNegativeButton(R.string.dismiss, (dialog, which) -> {
            binding.textViewStatusInformation
                    .setText(R.string.location_permission_denied_message);
            binding.progressBarRecyclerViewStatus.setVisibility(View.GONE);
        });
        builder.show();
    }

    private void restaurantsUpdate (Double latitude,
                                          Double longitude) {
        restaurantsViewModel.findRestaurants(latitude, longitude);
    }

    // The accepted best way was followed when adding click listeners to the recyclerview elements.
    // More detailed description given in RestaurantsRecyclerViewAdapter class.
    @Override
    public void onRestaurantClick(int position, View view) {
        //TODO
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        navController.navigate(R.id.action_masterRestaurantFragment_to_detailRestaurantFragment, bundle);
    }
}


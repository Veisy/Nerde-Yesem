package com.example.nerdeyesem.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nerdeyesem.R;
import com.example.nerdeyesem.adapters.RestaurantsRecyclerViewAdapter;
import com.example.nerdeyesem.databinding.FragmentRestaurantMasterBinding;
import com.example.nerdeyesem.model.RestaurantsModel;
import com.example.nerdeyesem.utils.GpsUtils;
import com.example.nerdeyesem.utils.Resource;
import com.example.nerdeyesem.viewmodel.LocationViewModel;
import com.example.nerdeyesem.viewmodel.RestaurantsViewModel;
import com.example.nerdeyesem.viewmodel.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

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
        binding = FragmentRestaurantMasterBinding
                .inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUserViewModel();
        initNavigationController(view);

        // Observe changes on LiveData<FirebaseUser> observable object.
        // If there is no user currently logged in, redirect to the login page
        initUserLiveDataObserver();

        initSignOutButton();
        initLocationViewModel();

        //Register callback that get the permission request.
        initRegisterPermissionCallback();

        initRestaurantViewModel();
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    private void initNavigationController(View view) {
        navController = Navigation.findNavController(view);
    }

    private void initSignOutButton() {
        binding.buttonSignOut.setOnClickListener(v -> userViewModel.logOut());
    }

    private void initUserLiveDataObserver() {
        userViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUserResource -> {
            if (firebaseUserResource.status == Resource.Status.SUCCESS
                    && firebaseUserResource.data != null) {
                binding.textViewWelcome.setText(firebaseUserResource.data.getEmail());
                //Make layout visible.
                binding.constraintLayoutFragmentRestaurantMaster.setVisibility(View.VISIBLE);

                //Check location permission, and get location if permission granted.
                getLocation();
            } else {
                navController.navigate(R.id.loginFragment);
            }
        });
    }

    private void initLocationViewModel() {
       locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
    }

    private void initRegisterPermissionCallback() {
        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog.
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        //We check permission anyway
                        //because Android says don't assume any system behaviour,
                        //and check permission every time.
                        getLocation();
                    } else {
                        binding.textViewLocation
                                .setText(R.string.location_permission_denied_message);
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
        // We care about this because we make this declaration in another observer.
        // There is a danger of memory leak by creating a new observer object here every time
        // when parent observer is triggered.
        if (!locationViewModel.getLocationLiveData().hasActiveObservers()) {
            locationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(),
                    locationModel -> {
                        if (locationModel != null) {
                            String coordinates = locationModel.getLatitude() + ", "
                                    + locationModel.getLongitude();
                            binding.textViewLocation
                                    .setText(coordinates);

                            //Get restaurants.
                            observeRestaurantUpdate(locationModel.getLatitude(),
                                    locationModel.getLongitude());
                        }
                    });
        }

        if(!locationViewModel.getIsGPSEnable().hasActiveObservers()) {
            locationViewModel.getIsGPSEnable().observe(getViewLifecycleOwner(),
                    aBoolean -> {
                        if (!aBoolean) {
                            binding.textViewLocation
                                    .setText(R.string.location_gps_disabled_message);
                        } else {
                            binding.textViewLocation
                                    .setText(R.string.location_gps_enabled);
                        }
                    });
        }
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

        builder.setNegativeButton(R.string.dismiss, (dialog, which) ->
                binding.textViewLocation
                .setText(R.string.location_permission_denied_message));
        builder.show();
    }

    private void initRestaurantViewModel() {
        restaurantsViewModel = new ViewModelProvider(requireActivity())
                .get(RestaurantsViewModel.class);
    }

    private void observeRestaurantUpdate (Double latitude,
                                          Double longitude) {

        restaurantsViewModel.findRestaurants(latitude, longitude);

        //If there is active observer, it means we have been here before and created observer object,
        //we do not memory leak by creating a new observer object and update the existing one.
        if (!restaurantsViewModel.getRestaurants().hasActiveObservers()) {
            restaurantsViewModel.getRestaurants().observe(getViewLifecycleOwner(),
                    listResource -> {
                        if (listResource.status == Resource.Status.SUCCESS) {

                                setRestaurantRecyclerView(listResource.data);

                        } else {
                            binding.textViewError.setText(listResource.message);
                            //TODO

                        }
                    });
        }
    }

    private void setRestaurantRecyclerView(RestaurantsModel restaurantsModel) {
        binding.recyclerViewRestaurants.setHasFixedSize(true);
        RestaurantsRecyclerViewAdapter restaurantsRecyclerViewAdapter =
                new RestaurantsRecyclerViewAdapter(requireContext(),
                restaurantsModel, this);
        binding.recyclerViewRestaurants
                .setAdapter(restaurantsRecyclerViewAdapter);
        binding.recyclerViewRestaurants
                .setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    // The accepted best way was followed when adding click listeners to the recyclerview elements.
    // More detailed description given in RestaurantsRecyclerViewAdapter class.
    @Override
    public void onRestaurantClick(int position, View view) {
        //TODO
        Toast.makeText(requireContext(), "Restaurant Cart Clicked", Toast.LENGTH_SHORT).show();
    }
}


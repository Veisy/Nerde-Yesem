package com.example.nerdeyesem.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nerdeyesem.R;
import com.example.nerdeyesem.databinding.FragmentRestaurantMasterBinding;
import com.example.nerdeyesem.repository.Resource;
import com.example.nerdeyesem.viewmodel.UserViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

public class RestaurantMasterFragment extends Fragment {
    private static final int DEFAULT_UPDATE_INTERVAL = 30;
    private static final int FAST_UPDATE_INTERVAL = 5;

    private FragmentRestaurantMasterBinding binding;
    private UserViewModel userViewModel;
    private NavController navController;

    // Reference to the return value of registerForActivityResult(),
    private ActivityResultLauncher<String> requestPermissionLauncher;

    //Google's API for location services. The majority of the app functions using this class.
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Location request is a config file for all settings related to FusedLocationProviderClient.
    private LocationRequest locationRequest;


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

        initFusedLocationClient();

        //Register callback that get the permission request.
        initRegisterPermissionCallback();

        initLocationRequestSettings();
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

    private void initFusedLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    private void initLocationRequestSettings() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(100 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
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
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        //TODO
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
        builder.setTitle(R.string.permission_title);
        builder.setMessage(R.string.permission_inform_message);
        builder.setPositiveButton(R.string.okay, (dialog, which) ->

                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION));

        builder.setNegativeButton(R.string.dismiss, (dialog, which) -> {
            //TODO
        });
        builder.show();
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            //After obtaining the location permit, we can get the location information.
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        // Got last known location
                        if (location != null) {
                            binding.textViewLocation.setText(location.getLatitude()
                                    + "\n" + location.getLongitude()
                                    + "\n" + location.getAccuracy());
                        }
                    });
        } else {
            requestPermission();
        }
    }


}


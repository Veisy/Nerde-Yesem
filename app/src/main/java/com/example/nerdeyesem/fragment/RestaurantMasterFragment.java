package com.example.nerdeyesem.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.example.nerdeyesem.viewmodel.LocationViewModel;
import com.example.nerdeyesem.viewmodel.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

public class RestaurantMasterFragment extends Fragment {
    private FragmentRestaurantMasterBinding binding;
    private UserViewModel userViewModel;
    private NavController navController;

    private LocationViewModel locationViewModel;

    // Reference to the return value of registerForActivityResult(),
    private ActivityResultLauncher<String> requestPermissionLauncher;

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
            startLocationUpdate();
        } else {
            requestPermission();
        }
    }

    private void startLocationUpdate() {
        locationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(),
                locationModel -> {
                    if (locationModel != null) {
                        binding.textViewLocation
                                .setText(locationModel.getLatitude()
                                        + "\n" + locationModel.getLongitude());
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

        builder.setNegativeButton(R.string.dismiss, (dialog, which) ->
                binding.textViewLocation
                .setText(R.string.location_permission_denied_message));
        builder.show();
    }

}


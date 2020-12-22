package com.example.nerdeyesem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nerdeyesem.R;
import com.example.nerdeyesem.databinding.FragmentRestaurantMasterBinding;
import com.example.nerdeyesem.repository.Resource;
import com.example.nerdeyesem.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

public class RestaurantMasterFragment extends Fragment {
    private FragmentRestaurantMasterBinding binding;
    private  UserViewModel userViewModel;
    private NavController navController;

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
                binding.textViewWelcome.setVisibility(View.VISIBLE);
                binding.buttonSignOut.setVisibility(View.VISIBLE);
            } else {
                navController.navigate(R.id.loginFragment);
            }
        });
    }
}


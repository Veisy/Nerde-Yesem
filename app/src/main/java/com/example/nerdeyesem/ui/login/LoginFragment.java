package com.example.nerdeyesem.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nerdeyesem.R;
import com.example.nerdeyesem.animation.FadeInAnimation;
import com.example.nerdeyesem.databinding.FragmentLoginBinding;
import com.example.nerdeyesem.utils.Resource;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;
    private  NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Hide toolbar of activity in this fragment.
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Animation for login field (fade in)
        FadeInAnimation.startAnimation(
                binding.constraintLayoutAuthFields,
                getResources().getInteger(android.R.integer.config_mediumAnimTime));

        initUserViewModel();
        initNavigationController(view);

        // Observe changes on LiveData<FirebaseUser> observable object.
        initUserLiveDataObserver();

        initLoginButton();
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
            if (firebaseUserResource.status == Resource.Status.SUCCESS
                    && firebaseUserResource.data != null) {
                //Successfully logged in.
                //Pop login screen from back stack.
                //TODO Revisit Back Stack Handling
                navController.popBackStack(R.id.masterRestaurantFragment, true);
                navController.navigate(R.id.masterRestaurantFragment);

            } else if (firebaseUserResource.status == Resource.Status.LOADING) {
                binding.progressBarLogin.setVisibility(View.VISIBLE);
            } else {
                binding.progressBarLogin.setVisibility(View.INVISIBLE);
                //Check error message.
                if (firebaseUserResource.status == Resource.Status.ERROR
                        && firebaseUserResource.message != null) {
                   if (Objects.equals(firebaseUserResource.message, FirebaseEmailAuthRepository.TOO_MANY_REQUEST)) {
                       Snackbar.make(requireView(),
                               getString(R.string.too_many_request), Snackbar.LENGTH_SHORT).show();
                   } else if (Objects.equals(firebaseUserResource.message, FirebaseEmailAuthRepository.INVALID_EMAIL)) {
                       setErrorMessage(binding.editTextEmailAddress, getString(R.string.invalid_email));
                   } else if (Objects.equals(firebaseUserResource.message, FirebaseEmailAuthRepository.WRONG_EMAIL)) {
                       setErrorMessage(binding.editTextEmailAddress, getString(R.string.wrong_email));
                   }else if (Objects.equals(firebaseUserResource.message, FirebaseEmailAuthRepository.WRONG_PASSWORD)) {
                       setErrorMessage(binding.editTextPassword, getString(R.string.wrong_password));
                   } else if (Objects.equals(firebaseUserResource.message, FirebaseEmailAuthRepository.LOGIN_FAILED)) {
                       Snackbar.make(requireView(),
                               getString(R.string.login_failed), Snackbar.LENGTH_SHORT).show();
                   }
                }
            }
        });
    }

    private void setErrorMessage(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
        editText.requestFocus();
    }

    //Login logic and error handling.
    private void initLoginButton() {
        binding.buttonLogin.setVisibility(View.VISIBLE);
        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.editTextEmailAddress.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            //Check if login fields are empty or not.
            //If not, call login function to update LiveData<FirebaseUser> observable object.
            if (!TextUtils.isEmpty(email)
                    && !TextUtils.isEmpty(password)) {
                email = binding.editTextEmailAddress.getText().toString();
                password = binding.editTextPassword.getText().toString();
                UserModel userModel  = new UserModel(email, password);
                login(userModel);
            } else {
                if (TextUtils.isEmpty(email))
                    binding.editTextEmailAddress.setError("Enter an email");
                if (TextUtils.isEmpty(password))
                    binding.editTextPassword.setError("Enter a password");
            }
        });
    }

    //Update LiveData<FirebaseUser> observable object.
    private void login(UserModel userModel) {
        userViewModel.login(userModel);
    }
}
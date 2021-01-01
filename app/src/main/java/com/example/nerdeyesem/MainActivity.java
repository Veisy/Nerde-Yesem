package com.example.nerdeyesem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.nerdeyesem.databinding.ActivityMainBinding;
import com.example.nerdeyesem.utils.GpsUtils;
import com.example.nerdeyesem.viewmodel.LocationViewModel;
import com.example.nerdeyesem.viewmodel.UserViewModel;

import static com.example.nerdeyesem.utils.GpsUtils.GPS_REQUEST;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.nerdeyesem.databinding.ActivityMainBinding binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //To set toolbar with navigation components, first we take reference to NavController.
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        setSupportActionBar(binding.toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController);
        binding.toolbar.setTitle(""); // for now, we will change the title in fragments.
    }

    //Enable navigate up.
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    // Inflate menu item.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.options_menu, menu);
       return true;
    }

    // On menu item clicked.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            UserViewModel userViewModel = new ViewModelProvider(this)
                    .get(UserViewModel.class);
            userViewModel.logOut();
            return true;
        } else {
            return false;
        }

    }

    // We had to override this method in single activity, because when we prompted the user to GPS setting to enable it,
    // we had to use startResolutionForResult() method and this method return callback value via onActivityResult.
    // And we cant get result from onActivityResult inside fragment, it does not work in Single Activity structure.
    // Therefore we needed to take result value from here and observe it with LiveData object inside LocationViewModel.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST) {
            LocationViewModel locationViewModel =
                    new ViewModelProvider(this).get(LocationViewModel.class);
            if (resultCode == Activity.RESULT_OK) {
               GpsUtils.isGPSEnabled = true;
            } else if (resultCode == Activity.RESULT_CANCELED) {
                GpsUtils.isGPSEnabled = false;
            }
            locationViewModel.getIsGPSEnable().setValue(GpsUtils.isGPSEnabled);
        }
    }
}
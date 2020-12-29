package com.example.nerdeyesem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nerdeyesem.databinding.ActivityMainBinding;
import com.example.nerdeyesem.utils.GpsUtils;
import com.example.nerdeyesem.viewmodel.LocationViewModel;

import static com.example.nerdeyesem.utils.GpsUtils.GPS_REQUEST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.nerdeyesem.databinding.ActivityMainBinding binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
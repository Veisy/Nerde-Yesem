package com.example.nerdeyesem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nerdeyesem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.nerdeyesem.databinding.ActivityMainBinding binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
package com.moutamid.controlsapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.controls.setOnClickListener(v -> {
            startActivity(new Intent(this, ControlsActivity.class));
        });

        binding.devices.setOnClickListener(v -> {
            startActivity(new Intent(this, DevicesActivity.class));
        });

        binding.values.setOnClickListener(v -> {
            startActivity(new Intent(this, ValuesActivity.class));
        });

        binding.stats.setOnClickListener(v -> {
            startActivity(new Intent(this, StatsActivity.class));
        });

    }
}
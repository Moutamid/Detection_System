package com.moutamid.controlsapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.FragmentMainBinding;
import com.moutamid.controlsapp.ui.ControlsActivity;
import com.moutamid.controlsapp.ui.DevicesActivity;
import com.moutamid.controlsapp.ui.StatsActivity;
import com.moutamid.controlsapp.ui.ValuesActivity;

public class MainFragment extends Fragment {
    FragmentMainBinding binding;
    public MainFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);

        binding.controls.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ControlsActivity.class));
        });

        binding.devices.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), DevicesActivity.class));
        });

        binding.values.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ValuesActivity.class));
        });

        binding.stats.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StatsActivity.class));
        });

        return binding.getRoot();

    }
}
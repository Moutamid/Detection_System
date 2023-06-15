package com.moutamid.controlsapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.ActivityControlsBinding;
import com.suke.widget.SwitchButton;

public class ControlsActivity extends AppCompatActivity {
    ActivityControlsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);
        Constants.showDialog();

        Constants.databaseReference().child("state").get().addOnSuccessListener(dataSnapshot -> {
            Constants.dismissDialog();
            boolean humidity = dataSnapshot.child("humidity").getValue(Boolean.class);
            boolean temperature = dataSnapshot.child("temperature").getValue(Boolean.class);
            boolean gas = dataSnapshot.child("gas").getValue(Boolean.class);
            binding.switchButtonHumidity.setChecked(humidity);
            binding.switchButtonTemp.setChecked(temperature);
            binding.switchButtonGas.setChecked(gas);
        }).addOnFailureListener(e -> {
            Constants.dismissDialog();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        binding.back.setOnClickListener(v -> finish());

        binding.switchButtonHumidity.setOnCheckedChangeListener((view, isChecked) -> {
            Constants.showDialog();
            Constants.databaseReference().child("state").child("humidity").setValue(isChecked)
                    .addOnSuccessListener(unused ->  Constants.dismissDialog())
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(ControlsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.switchButtonTemp.setOnCheckedChangeListener((view, isChecked) -> {
            Constants.showDialog();
            Constants.databaseReference().child("state").child("temperature").setValue(isChecked)
                    .addOnSuccessListener(unused ->  Constants.dismissDialog())
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(ControlsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.switchButtonGas.setOnCheckedChangeListener((view, isChecked) -> {
            Constants.showDialog();
            Constants.databaseReference().child("state").child("gas").setValue(isChecked)
                    .addOnSuccessListener(unused ->  Constants.dismissDialog())
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(ControlsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }
}
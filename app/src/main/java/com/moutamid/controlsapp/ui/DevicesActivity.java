package com.moutamid.controlsapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.ActivityDevicesBinding;

public class DevicesActivity extends AppCompatActivity {
    ActivityDevicesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> finish());

        binding.device1.setOnClickListener(v -> {
            showDialog();
        });
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.start_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button auto = dialog.findViewById(R.id.auto);
        Button manual = dialog.findViewById(R.id.manual);

        auto.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        });

        manual.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(DevicesActivity.this, ControlsActivity.class));
        });

        dialog.show();

    }
}
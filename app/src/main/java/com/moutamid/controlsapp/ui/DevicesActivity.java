package com.moutamid.controlsapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.ActivityDevicesBinding;

import java.util.HashMap;
import java.util.Map;

public class DevicesActivity extends AppCompatActivity {
    ActivityDevicesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

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
            Constants.showDialog();
            Constants.databaseReference().child("state").child("light").setValue(true)
                    .addOnSuccessListener(unused -> {
                        Map<String, Object> obj = new HashMap<>();
                        obj.put("text", "You auto start the device");
                        Constants.databaseReference().child(Constants.notifications).push().setValue(obj).addOnSuccessListener(unused1 -> Constants.dismissDialog());
                    })
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(DevicesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            Constants.databaseReference().child("state").child("buzzer").setValue(true)
                    .addOnSuccessListener(unused -> {

                    })
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(DevicesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        manual.setOnClickListener(v -> {
            dialog.dismiss();
            Constants.showDialog();
            Map<String, Object> obj = new HashMap<>();
            obj.put("text", "You manually started device");
            Constants.databaseReference().child(Constants.notifications).push().setValue(obj).addOnSuccessListener(unused1 -> {
                Constants.dismissDialog();
                startActivity(new Intent(DevicesActivity.this, ControlsActivity.class));
            });
        });

        dialog.show();

    }
}
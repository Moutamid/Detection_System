package com.moutamid.controlsapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.ActivityControlsBinding;
import com.suke.widget.SwitchButton;

import java.util.HashMap;
import java.util.Map;

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
            if (dataSnapshot.exists()){
                boolean humidity = dataSnapshot.child("light").getValue(Boolean.class);
                boolean gas = dataSnapshot.child("buzzer").getValue(Boolean.class);
                binding.switchButtonHumidity.setChecked(humidity);
                binding.switchButtonGas.setChecked(gas);
            }
        }).addOnFailureListener(e -> {
            Constants.dismissDialog();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        binding.back.setOnClickListener(v -> finish());

        binding.switchButtonHumidity.setOnCheckedChangeListener((view, isChecked) -> {
            Constants.showDialog();
            Constants.databaseReference().child("state").child("light").setValue(isChecked)
                    .addOnSuccessListener(unused -> {
                        String text = isChecked ? "On" : "Off";
                        Map<String, Object> obj = new HashMap<>();
                        obj.put("text", "You turn light " + text);
                        Constants.databaseReference().child(Constants.notifications).push().setValue(obj).addOnSuccessListener(unused1 -> Constants.dismissDialog());
                    })
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(ControlsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


        binding.switchButtonGas.setOnCheckedChangeListener((view, isChecked) -> {
            Constants.showDialog();
            Constants.databaseReference().child("state").child("buzzer").setValue(isChecked)
                    .addOnSuccessListener(unused -> {
                        String text = isChecked ? "On" : "Off";
                        Map<String, Object> obj = new HashMap<>();
                        obj.put("text", "You turn buzzer " + text);
                        Constants.databaseReference().child(Constants.notifications).push().setValue(obj).addOnSuccessListener(unused1 -> Constants.dismissDialog());
                    })
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(ControlsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }
}
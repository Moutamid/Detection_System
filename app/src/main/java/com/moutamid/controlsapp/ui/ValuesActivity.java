package com.moutamid.controlsapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.ActivityValuesBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ValuesActivity extends AppCompatActivity {
    ActivityValuesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValuesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);
        Constants.showDialog();

        Constants.databaseReference().child("values").child("humidity").get()
                        .addOnSuccessListener(dataSnapshot -> {
                            String max = dataSnapshot.child("max").getValue(String.class);
                            String min = dataSnapshot.child("min").getValue(String.class);

                            binding.humdMax.getEditText().setText(max);
                            binding.humdMin.getEditText().setText(min);

                            Constants.dismissDialog();

                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Constants.databaseReference().child("values").child("temperature").get()
                        .addOnSuccessListener(dataSnapshot -> {
                            String max = dataSnapshot.child("max").getValue(String.class);
                            String min = dataSnapshot.child("min").getValue(String.class);

                            binding.tempMax.getEditText().setText(max);
                            binding.tempMin.getEditText().setText(min);

                            Constants.dismissDialog();

                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Constants.databaseReference().child("values").child("gas").get()
                        .addOnSuccessListener(dataSnapshot -> {
                            String max = dataSnapshot.child("max").getValue(String.class);
                            String min = dataSnapshot.child("min").getValue(String.class);

                            binding.gasMax.getEditText().setText(max);
                            binding.gasMin.getEditText().setText(min);

                            Constants.dismissDialog();

                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.back.setOnClickListener(v -> finish());

        binding.update.setOnClickListener(v -> {
            Constants.showDialog();
            String humiMax = binding.humdMax.getEditText().getText().toString().isEmpty() ? "0.0" : binding.humdMax.getEditText().getText().toString();
            String humiMin = binding.humdMin.getEditText().getText().toString().isEmpty() ? "0.0" : binding.humdMin.getEditText().getText().toString();

            String tempMax = binding.tempMax.getEditText().getText().toString().isEmpty() ? "0.0" : binding.tempMax.getEditText().getText().toString();
            String tempMin = binding.tempMin.getEditText().getText().toString().isEmpty() ? "0.0" : binding.tempMin.getEditText().getText().toString();

            String gasMax = binding.gasMax.getEditText().getText().toString().isEmpty() ? "0.0" : binding.gasMax.getEditText().getText().toString();
            String gasMin = binding.gasMin.getEditText().getText().toString().isEmpty() ? "0.0" : binding.gasMin.getEditText().getText().toString();

            Map<String, Object> humidity = new HashMap<>();
            humidity.put("max", humiMax);
            humidity.put("min", humiMin);

            Map<String, Object> temperature = new HashMap<>();
            temperature.put("max", tempMax);
            temperature.put("min", tempMin);

            Map<String, Object> gas = new HashMap<>();
            gas.put("max", gasMax);
            gas.put("min", gasMin);

            Constants.databaseReference().child(Constants.values).child(Constants.humidity)
                    .setValue(humidity).addOnFailureListener(e -> Constants.dismissDialog()).addOnSuccessListener(unused -> Constants.dismissDialog());
            Constants.databaseReference().child(Constants.values).child(Constants.temperature)
                    .setValue(temperature).addOnFailureListener(e -> Constants.dismissDialog()).addOnSuccessListener(unused -> Constants.dismissDialog());
            Constants.databaseReference().child(Constants.values).child(Constants.gas)
                    .setValue(gas).addOnFailureListener(e -> Constants.dismissDialog()).addOnSuccessListener(unused -> Constants.dismissDialog());
            Map<String, Object> obj = new HashMap<>();
            obj.put("text", "You adjust the temperature/humidity values");
            Constants.databaseReference().child(Constants.notifications).push().setValue(obj).addOnSuccessListener(unused1 -> Constants.dismissDialog());
        });

    }
}
package com.moutamid.controlsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.adapters.NotificationAdapter;
import com.moutamid.controlsapp.databinding.FragmentNotificationBinding;
import com.moutamid.controlsapp.databinding.FragmentSignupBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public class NotificationFragment extends Fragment {
    FragmentNotificationBinding binding;
    ArrayList<String> list;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(getLayoutInflater(), container, false);

        list = new ArrayList<>();

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        Constants.databaseReference().child(Constants.notifications).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String text = dataSnapshot.child("text").getValue(String.class);
                        list.add(text);
                    }
                }
                Collections.reverse(list);
                NotificationAdapter adapter = new NotificationAdapter(binding.getRoot().getContext(), list);
                binding.recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

}
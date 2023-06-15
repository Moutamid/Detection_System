package com.moutamid.controlsapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.MainActivity;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.FragmentLoginBinding;
import com.moutamid.controlsapp.ui.DashboardActivity;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);
        MainActivity activity = (MainActivity) getActivity();

        Constants.initDialog(requireContext());

        binding.createAccount.setOnClickListener(v -> {
            if (activity!=null){
                ViewPager vp = activity.findViewById(R.id.viewPager);
                vp.setCurrentItem(1);
            }
        });

        binding.login.setOnClickListener(v -> {
            if (binding.email.getEditText().getText().toString().isEmpty()){
                binding.email.getEditText().setError("Email is required");
            } else {
                Constants.showDialog();
                Constants.auth().signInWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    Constants.dismissDialog();
                    startActivity(new Intent(requireContext(), DashboardActivity.class));
                    activity.finish();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        return binding.getRoot();
    }
}
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
import com.moutamid.controlsapp.databinding.FragmentSignupBinding;
import com.moutamid.controlsapp.models.UserModel;
import com.moutamid.controlsapp.ui.DashboardActivity;

public class SignupFragment extends Fragment {
    FragmentSignupBinding binding;
    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(getLayoutInflater(), container, false);

        MainActivity activity = (MainActivity) getActivity();

        Constants.initDialog(requireContext());

        binding.createAccount.setOnClickListener(v -> {
            if (activity!=null){
                ViewPager vp = activity.findViewById(R.id.viewPager);
                vp.setCurrentItem(0);
            }
        });

        binding.signup.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    UserModel model = new UserModel(
                            Constants.auth().getCurrentUser().getUid(),
                            binding.name.getEditText().getText().toString(),
                            binding.email.getEditText().getText().toString(),
                            binding.password.getEditText().getText().toString()
                    );
                    Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                            .setValue(model).addOnSuccessListener(unused -> {
                                Constants.dismissDialog();
                                Toast.makeText(activity, "User created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(requireContext(), DashboardActivity.class));
                                activity.finish();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        return binding.getRoot();
    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.getEditText().setError("Email is required");
            return false;
        }
        if (binding.name.getEditText().getText().toString().isEmpty()){
            binding.name.getEditText().setError("Username is required");
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.getEditText().setError("Password is required");
            return false;
        }
        return true;
    }
}
package com.example.finelspruject.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finelspruject.R;

public class SettingsFragment extends Fragment {

    Switch switchNotifications;
    Button btnChangePassword, btnBack;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        switchNotifications = view.findViewById(R.id.switchNotifications);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnBack = view.findViewById(R.id.btnBack);

        // Back button action - Remove the fragment
        btnBack.setOnClickListener(v -> {
            // Remove the current fragment completely from the FragmentManager
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.remove(SettingsFragment.this); // Remove the current fragment
            transaction.commit();
        });
        // Toggle Notifications Switch
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getActivity(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Change Password Action
        btnChangePassword.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Change Password feature coming soon!", Toast.LENGTH_SHORT).show()
        );



        return view;
    }
}

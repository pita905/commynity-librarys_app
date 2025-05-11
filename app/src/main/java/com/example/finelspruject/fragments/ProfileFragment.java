package com.example.finelspruject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finelspruject.AdminPanelActivity;
import com.example.finelspruject.DashboardActivity;
import com.example.finelspruject.FirebaseHelper;
import com.example.finelspruject.HomeActivity;
import com.example.finelspruject.LoginActivity;
import com.example.finelspruject.R;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private static final String ARG_USERNAME = "username"; // Key for passing username
    private String username;

    TextView txtName, txtEmail, txtUsername;
    Button btnLogout, btnBack;
    FirebaseHelper firebaseHelper;

    // Factory method to create a new instance of the fragment with a username
    public static ProfileFragment newInstance(String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve username from arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        if (username == null) {
            Log.e("ProfileFragment", "Username is null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseHelper = FirebaseHelper.getInstance();
        // Initialize views
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnBack = view.findViewById(R.id.btnBack);

        String username = getArguments() != null ? getArguments().getString("username") : null;
        if (username == null) {
            Log.e("ProfileFragment", "Username is null");
            Toast.makeText(getActivity(), "Error: User not found.", Toast.LENGTH_SHORT).show();
            return view;
        }
        // Fetch user details from Firebase
        firebaseHelper.getUserDetailsByUsername(username, new FirebaseHelper.OnUserDetailsListener() {
            @Override
            public void onUserDetailsRetrieved(boolean success, HashMap<String, String> userDetails) {
                if (!success || userDetails.isEmpty()) {
                    Log.e("ProfileFragment", "User details not found for username: " + username);
                    Toast.makeText(getActivity(), "Error: User details not found.", Toast.LENGTH_SHORT).show();
                } else {
                    // Display user details
                    txtName.setText("Name: " + userDetails.get("name"));
                    txtEmail.setText("Email: " + userDetails.get("email"));
                    txtUsername.setText("Username: " + userDetails.get("username"));
                }
            }
        });
        // Back button action - Remove the fragment
        btnBack.setOnClickListener(v -> {
            // Remove the current fragment completely from the FragmentManager
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.remove(ProfileFragment.this); // Remove the current fragment
            transaction.commit();
        });

        // Logout button action
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });

        return view;
    }



}


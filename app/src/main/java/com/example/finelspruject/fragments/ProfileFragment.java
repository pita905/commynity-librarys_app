package com.example.finelspruject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finelspruject.DatabaseHelper;
import com.example.finelspruject.LoginActivity;
import com.example.finelspruject.R;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private static final String ARG_USERNAME = "username"; // Key for passing username
    private String username;

    TextView txtName, txtEmail, txtUsername;
    Button btnLogout;
    DatabaseHelper dbHelper;

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
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME); // Retrieve username
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnLogout = view.findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(getActivity());

        // Fetch user details from the database
        HashMap<String, String> userDetails = dbHelper.getUserDetails(username);

        // Display user details
        txtName.setText("Name: " + userDetails.get("name"));
        txtEmail.setText("Email: " + userDetails.get("email"));
        txtUsername.setText("Username: " + userDetails.get("username"));

        // Logout button action
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });

        return view;
    }
}

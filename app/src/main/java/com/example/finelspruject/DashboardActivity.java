package com.example.finelspruject;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finelspruject.fragments.DashboardFragment;
import com.example.finelspruject.fragments.ProfileFragment;
import com.example.finelspruject.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.finelspruject.R;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity {

    Button btnSearchLibraries, btnNearbyLibraries, btnCategories, btnMapView, btnAdminPanel, btnLogout;
    TextView txtAppTitle; // For triggering the hidden admin access
    private static final String ADMIN_PASSWORD = "1234"; // Admin password
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //  buttons
        btnSearchLibraries = findViewById(R.id.btnSearchLibraries);
        btnNearbyLibraries = findViewById(R.id.btnNearbyLibraries);
        btnCategories = findViewById(R.id.btnCategories);
        btnMapView = findViewById(R.id.btnMapView);
        btnAdminPanel = findViewById(R.id.btnAdminPanel);
        btnLogout = findViewById(R.id.btnLogout);
        txtAppTitle = findViewById(R.id.txtAppTitle);

        // hide the Admin Panel button
        btnAdminPanel.setVisibility(View.GONE);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment
        loadFragment(new DashboardFragment());

        // Handle navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new SettingsFragment();
            }

            return loadFragment(selectedFragment);

        });

        // Secret action: Long-press the title to show password prompt
        txtAppTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPasswordDialog(); // Show password prompt
                return true;
            }
        });

        // Admin Panel Button Click
        btnAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AdminPanelActivity.class));
            }
        });


        // Navigate to Search Libraries Page
        btnSearchLibraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        // Navigate to Nearby Libraries Page
        btnNearbyLibraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(DashboardActivity.this, NearbyLibrariesActivity.class));
            }
        });

        // Navigate to Categories Page
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(DashboardActivity.this, CategoriesActivity.class));
            }
        });

        // Navigate to Map View Page
        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(DashboardActivity.this, MapViewActivity.class));
            }
        });

        // Log out: Navigate back to Login Page
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                startActivity(intent);
                finish(); // Close DashboardActivity
            }
        });
    }


    /**
     * Method to load the selected fragment.
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    // Show a password dialog to authenticate access to the Admin Panel.

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Admin Access");
        builder.setMessage("Enter the Admin Password:");

        // Add an input field for the password
        final View customView = getLayoutInflater().inflate(R.layout.dialog_password_input, null);
        builder.setView(customView);

        // Handle password submission
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the entered password
                String enteredPassword = ((android.widget.EditText) customView.findViewById(R.id.edtPassword)).getText().toString();

                // Check if the password matches
                if (enteredPassword.equals(ADMIN_PASSWORD)) {
                    btnAdminPanel.setVisibility(View.VISIBLE); // Show the Admin Panel button
                    Toast.makeText(DashboardActivity.this, "Access Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cancel option
        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        builder.create().show();
    }
    }





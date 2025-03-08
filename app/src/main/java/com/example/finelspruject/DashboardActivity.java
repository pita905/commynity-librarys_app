package com.example.finelspruject;


import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.finelspruject.fragments.ProfileFragment;
import com.example.finelspruject.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    Button btnSearchLibraries, btnMapView, btnAdminPanel;
    TextView txtAppTitle; // For triggering the hidden admin access
    private static final String ADMIN_PASSWORD = "1234"; // Admin password
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //  buttons
        btnSearchLibraries = findViewById(R.id.btnSearchLibraries);
        btnMapView = findViewById(R.id.btnMapView);
        btnAdminPanel = findViewById(R.id.btnAdminPanel);
        txtAppTitle = findViewById(R.id.txtAppTitle);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnAdminPanel.setVisibility(View.GONE);

        String username = getIntent().getStringExtra("username");
        if (username == null) {
            Log.e("DashboardActivity", "Username is null");
            Toast.makeText(this, "Error: Username not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Exit to prevent undefined behavior
            return;
        }



        // Handle navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = ProfileFragment.newInstance(username); // Pass username to ProfileFragment
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new SettingsFragment();
            } else if (item.getItemId() == R.id.navigation_exit) {
                // Exit the app completely
                finishAffinity();
                System.exit(0);
                return true; // Event handled, so return true
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
               Intent intent = new Intent(DashboardActivity.this, AdminPanelActivity.class);
               intent.putExtra("username", username);
               startActivity(intent);
               finish();
           }
       });


        // Navigate to Search Libraries Page
        btnSearchLibraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure username is passed to SearchActivity
                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                intent.putExtra("username", username); // Pass the username
                startActivity(intent);
                finish();
            }
        });


        // Navigate to Map View Page
        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MapsActivity.class);
                intent.putExtra("username", username); // Pass the username
                startActivity(intent);
                finish();            }
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
        Log.e("DashboardActivity", "loadFragment: Fragment is null");
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






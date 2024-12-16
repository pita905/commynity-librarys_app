package com.example.finelspruject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    // Buttons for different dashboard options
    Button btnSearchLibraries, btnNearbyLibraries, btnCategories, btnMapView, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Set the layout for the dashboard

        // Initialize buttons
        btnSearchLibraries = findViewById(R.id.btnSearchLibraries);
        btnNearbyLibraries = findViewById(R.id.btnNearbyLibraries);
        btnCategories = findViewById(R.id.btnCategories);
        btnMapView = findViewById(R.id.btnMapView);
        btnLogout = findViewById(R.id.btnLogout);

        // Navigate to Search Libraries Page
        btnSearchLibraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(DashboardActivity.this, SearchLibrariesActivity.class));
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
}

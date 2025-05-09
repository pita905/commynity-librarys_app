package com.example.finelspruject;

import android.content.Intent; // Used to navigate to other activities
import android.content.pm.PackageManager;
import android.os.Bundle;      // To handle the activity's state
import android.view.View;      // For handling button click events
import android.widget.Button;  // UI Button component

import androidx.appcompat.app.AppCompatActivity; // Base class for AppCompat activities
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {

    // Declare buttons for Login and Register
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the parent class's onCreate method
        setContentView(R.layout.activity_homepage); // Set the layout file for the HomeActivity


        // Initialize buttons using their IDs defined in the XML layout
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Set click listener for the Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the LoginActivity when Login button is clicked
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the Register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the RegisterActivity when Register button is clicked
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
            }
        });
    }
}

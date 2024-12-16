package com.example.finelspruject;

import android.content.Intent;
import android.database.Cursor; // Used to query the SQLite database
import android.os.Bundle;       // For handling activity states
import android.view.View;       // For handling button click events
import android.widget.Button;   // UI Button component
import android.widget.EditText; // Input fields for username and password
import android.widget.Toast;    // Display messages to the user

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Declare input fields and login button
    EditText edtUsername, edtPassword;
    Button btnLogin;

    // Declare DatabaseHelper for accessing the SQLite database
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Set the layout file for LoginActivity

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Link UI elements from XML layout to Java variables
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Set click listener for the Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values from the user
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // Check if credentials are valid using the DatabaseHelper
                if (dbHelper.validateUser(username, password)) {
                    if (dbHelper.validateUser(username, password)) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Navigate to Dashboard
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish(); // Close LoginActivity
                    }
                } else {
                    // Show error if credentials are invalid
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

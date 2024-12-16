package com.example.finelspruject;

import android.os.Bundle;       // For handling activity states
import android.view.View;       // For handling button click events
import android.widget.Button;   // UI Button component
import android.widget.EditText; // Input fields for username and passwords
import android.widget.Toast;    // Display messages to the user

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // Declare input fields and register button
    EditText edtUsername, edtPassword, edtConfirmPassword;
    Button btnRegister;

    // Declare DatabaseHelper for accessing the SQLite database
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Set the layout file for RegisterActivity

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Link UI elements from XML layout to Java variables
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Set click listener for the Register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values from the user
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                // Check if all fields are filled
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                // Check if passwords match
                else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
                // Check if username already exists
                else if (dbHelper.checkUserExists(username)) {
                    Toast.makeText(RegisterActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                }
                // Attempt to register the user
                else {
                    boolean success = dbHelper.registerUser(username, password);
                    if (success) {
                        // Display success message and finish activity
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        finish(); // Close RegisterActivity and return to LoginActivity
                    } else {
                        // Display failure message
                        Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

package com.example.finelspruject;

import android.content.Intent;
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
    Button btnBack;
    // Firebase helper for authentication
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Set the layout file for LoginActivity

        // Initialize Firebase helper
        firebaseHelper = FirebaseHelper.getInstance();

        // Link UI elements from XML layout to Java variables
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);

        // Set click listener for the Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values from the user
                final String username = edtUsername.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Check if credentials are valid using Firebase
                // Note: Firebase Authentication uses email for login, not username
                // So we need to construct an email from username or get the associated email
                
                // Query Firestore to get the email associated with this username
                firebaseHelper.getEmailByUsername(username, new FirebaseHelper.OnEmailRetrievedListener() {
                    @Override
                    public void onEmailRetrieved(boolean success, String email) {
                        if (success && email != null) {
                            // Now attempt to sign in with Firebase using the retrieved email
                            firebaseHelper.validateUser(email, password, new FirebaseHelper.OnLoginListener() {
                                @Override
                                public void onLoginComplete(boolean success, String message, String userId) {
                                    if (success) {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        
                                        // Ensure username is passed to DashboardActivity
                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                        intent.putExtra("username", username); // Pass the username
                                        intent.putExtra("userId", userId); // Pass the Firebase user ID
                                        startActivity(intent);
                                        finish(); // Close LoginActivity
                                    } else {
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Username not found or could not retrieve email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    btnBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        }
    });
    }
}

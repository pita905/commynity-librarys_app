package com.example.finelspruject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminPanelActivity extends AppCompatActivity {

    EditText edtLibraryName, edtLibraryLocation;
    Button btnAddLibrary;
    Button btnBack;
    FirebaseLibraryHelper firebaseHelper; // Use Firebase instead of SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Initialize UI components
        edtLibraryName = findViewById(R.id.edtLibraryName);
        edtLibraryLocation = findViewById(R.id.edtLibraryLocation);
        btnAddLibrary = findViewById(R.id.btnAddLibrary);
        btnBack = findViewById(R.id.btnBack);
        firebaseHelper = FirebaseLibraryHelper.getInstance(); // Initialize Firebase helper


        String username = getIntent().getStringExtra("username");
        if (username == null) {
            Log.e("AdminPanelActivity", "Username is null");
            Toast.makeText(this, "Error: Username not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Exit to prevent undefined behavior
            return;
        }

        edtLibraryLocation.setOnClickListener(new View.OnClickListener() {
            private long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 300) { // double click detected
                    Intent intent = new Intent(AdminPanelActivity.this, MapsActivity.class);
                    intent.putExtra("mode", "select"); // pass a flag to enable selection mode
                    intent.putExtra("username", username); // Pass the username
                    startActivityForResult(intent, 100); // unique request code
                }
                lastClickTime = clickTime;
            }
        });

        // Handle Add Library button click
        btnAddLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String libraryName = edtLibraryName.getText().toString().trim();
                String libraryLocation = edtLibraryLocation.getText().toString().trim();

                if (TextUtils.isEmpty(libraryName) || TextUtils.isEmpty(libraryLocation)) {
                    Toast.makeText(AdminPanelActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the library to Firestore
                    firebaseHelper.addLibrary(libraryName, libraryLocation, new FirebaseLibraryHelper.OnLibraryAddedListener() {
                        @Override
                        public void onLibraryAdded(boolean success, String message, String libraryId) {
                            if (success) {
                                Toast.makeText(AdminPanelActivity.this, "Library added successfully!", Toast.LENGTH_SHORT).show();
                                edtLibraryName.setText("");
                                edtLibraryLocation.setText("");
                            } else {
                                Toast.makeText(AdminPanelActivity.this, "Failed to add library: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanelActivity.this, DashboardActivity.class);
                intent.putExtra("username", username); // Pass the username
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            String selectedAddress = data.getStringExtra("selected_address");
            if (selectedAddress != null) {
                edtLibraryLocation.setText(selectedAddress);
            }
        }
    }

}

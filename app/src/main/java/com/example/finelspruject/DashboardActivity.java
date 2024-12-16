package com.example.finelspruject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnSearchLibraries, btnNearbyLibraries, btnCategories, btnMapView, btnAdminPanel, btnLogout;
    TextView txtAppTitle; // For triggering the hidden admin access
    private static final String ADMIN_PASSWORD = "1234"; // Admin password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize buttons
        btnSearchLibraries = findViewById(R.id.btnSearchLibraries);
        btnNearbyLibraries = findViewById(R.id.btnNearbyLibraries);
        btnCategories = findViewById(R.id.btnCategories);
        btnMapView = findViewById(R.id.btnMapView);
        btnAdminPanel = findViewById(R.id.btnAdminPanel);
        btnLogout = findViewById(R.id.btnLogout);
        txtAppTitle = findViewById(R.id.txtAppTitle);

        // Initially hide the Admin Panel button
        btnAdminPanel.setVisibility(View.GONE);

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

        // Logout Button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
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

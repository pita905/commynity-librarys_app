package com.example.finelspruject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminPanelActivity extends AppCompatActivity {

    EditText edtLibraryName, edtLibraryLocation;
    Button btnAddLibrary;
    LibraryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Initialize UI components
        edtLibraryName = findViewById(R.id.edtLibraryName);
        edtLibraryLocation = findViewById(R.id.edtLibraryLocation);
        btnAddLibrary = findViewById(R.id.btnAddLibrary);
        dbHelper = new LibraryDatabaseHelper(this);

        // Handle Add Library button click
        btnAddLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String libraryName = edtLibraryName.getText().toString().trim();
                String libraryLocation = edtLibraryLocation.getText().toString().trim();

                if (TextUtils.isEmpty(libraryName) || TextUtils.isEmpty(libraryLocation)) {
                    Toast.makeText(AdminPanelActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the library to the database
                    long result = dbHelper.addLibrary(libraryName, libraryLocation);
                    if (result != -1) {
                        Toast.makeText(AdminPanelActivity.this, "Library added successfully!", Toast.LENGTH_SHORT).show();
                        edtLibraryName.setText("");
                        edtLibraryLocation.setText("");
                    } else {
                        Toast.makeText(AdminPanelActivity.this, "Failed to add library!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

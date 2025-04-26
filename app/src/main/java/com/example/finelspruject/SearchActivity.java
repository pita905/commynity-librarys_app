package com.example.finelspruject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText edtSearchBar;            // Search input field
    ListView listViewResults;         // ListView to display results
    ArrayAdapter<String> listAdapter; // Adapter for ListView
    LibraryDatabaseHelper dbHelper;   // Database helper
    Button btnBack;
    List<Library> libraryList = new ArrayList<>(); // List to store search results
    List<String> libraryNames = new ArrayList<>(); // List to store library names for display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize UI components
        edtSearchBar = findViewById(R.id.edtSearchBar);
        listViewResults = findViewById(R.id.listViewResults);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new LibraryDatabaseHelper(this); // Initialize database helper

        // Set up ListView adapter
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, libraryNames);
        listViewResults.setAdapter(listAdapter);

        // Fetch all libraries initially
        fetchLibraries("");

        String username = getIntent().getStringExtra("username");
        if (username == null) {
            Log.e("DashboardActivity", "Username is null");
            Toast.makeText(this, "Error: Username not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Exit to prevent undefined behavior
            return;
        }

        // Add TextWatcher to filter results as user types
        edtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchLibraries(s.toString()); // Fetch libraries matching the query
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Handle back button click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure username is passed to DashboardActivity
                Intent intent = new Intent(SearchActivity.this, DashboardActivity.class);
                intent.putExtra("username", username); // Pass the username
                startActivity(intent);
                finish();
            }
        });

        // Set up item click listener for ListView
        listViewResults.setOnItemClickListener((parent, view, position, id) -> {
            // Get the clicked library object
            Library selectedLibrary = libraryList.get(position);

            // Navigate to LibraryDetailsActivity
            Intent intent = new Intent(SearchActivity.this, LibraryDetailsActivity.class);
            intent.putExtra("library_name", selectedLibrary.getName());
            intent.putExtra("library_location", selectedLibrary.getLocation());
            intent.putExtra("library_id", selectedLibrary.getId());
            // Pass the username to the LibraryDetailsActivity
            intent.putExtra("username", getIntent().getStringExtra("username"));

            startActivity(intent);
        });
    }

    /**
     * Fetch libraries based on search query and update the ListView.
     * @param query The search query entered by the user.
     */
    private void fetchLibraries(String query) {
        libraryList.clear(); // Clear previous results
        libraryNames.clear(); // Clear previous display list

        // Fetch all libraries from the database
        List<Library> allLibraries = dbHelper.getAllLibraries();

        // Filter libraries based on name or location
        for (Library library : allLibraries) {
            if (library.getName().toLowerCase().contains(query.toLowerCase()) ||
                    library.getLocation().toLowerCase().contains(query.toLowerCase())) {
                libraryList.add(library);
                libraryNames.add(library.getName()); // Add the library name for display
            }
        }

        listAdapter.notifyDataSetChanged(); // Notify adapter about data changes
    }


}

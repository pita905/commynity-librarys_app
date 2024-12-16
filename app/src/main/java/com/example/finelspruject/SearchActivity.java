package com.example.finelspruject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finelspruject.adapters.LibraryAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText edtSearchBar;            // Search input field
    RecyclerView recyclerViewResults; // RecyclerView to display results
    LibraryAdapter libraryAdapter;    // Adapter for RecyclerView
    LibraryDatabaseHelper dbHelper;   // Database helper

    List<Library> libraryList = new ArrayList<>(); // List to store search results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize UI components
        edtSearchBar = findViewById(R.id.edtSearchBar);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);

        dbHelper = new LibraryDatabaseHelper(this); // Initialize database helper

        // Set up RecyclerView
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        libraryAdapter = new LibraryAdapter(libraryList, this); // Pass "this" as context
        recyclerViewResults.setAdapter(libraryAdapter);
        recyclerViewResults.setAdapter(libraryAdapter);

        // Fetch all libraries initially
        fetchLibraries("");

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
    }

    /**
     * Fetch libraries based on search query and update the RecyclerView.
     * @param query The search query entered by the user.
     */
    private void fetchLibraries(String query) {
        libraryList.clear(); // Clear previous results

        // Fetch all libraries from the database
        List<Library> allLibraries = dbHelper.getAllLibraries();

        // Filter libraries based on name or location
        for (Library library : allLibraries) {
            if (library.getName().toLowerCase().contains(query.toLowerCase()) ||
                    library.getLocation().toLowerCase().contains(query.toLowerCase())) {
                libraryList.add(library);
            }
        }

        libraryAdapter.notifyDataSetChanged(); // Notify adapter about data changes
    }
}

package com.example.finelspruject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finelspruject.Book;
import com.example.finelspruject.adapters.BookAdapter;

import java.util.List;

public class LibraryDetailsActivity extends AppCompatActivity {
    Button btnBack, btnAddBook , btnNavigate;
    TextView txtLibraryName, txtLibraryLocation;
    ListView listViewBooks;
    FirebaseLibraryHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_details);

        // Initialize views
        txtLibraryName = findViewById(R.id.txtLibraryName);
        txtLibraryLocation = findViewById(R.id.txtLibraryLocation);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnNavigate = findViewById(R.id.btnNavigate);
        btnBack = findViewById(R.id.btnBack);
        listViewBooks = findViewById(R.id.listViewBooks);

        firebaseHelper = FirebaseLibraryHelper.getInstance();

        String username = getIntent().getStringExtra("username");
        if (username == null) {
            Log.e("LibraryDetailsActivity", "Username is null");
            Toast.makeText(this, "Error: Username not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Exit to prevent undefined behavior
            return;
        }
        // Retrieve library details from intent
        String libraryName = getIntent().getStringExtra("library_name");
        String libraryLocation = getIntent().getStringExtra("library_location");
        String libraryId = getIntent().getStringExtra("library_id"); // Library ID to fetch books

        // Set library details
        txtLibraryName.setText("Library Name: " + libraryName);
        txtLibraryLocation.setText("Location: " + libraryLocation);



        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String libraryId = getIntent().getStringExtra("library_id"); // Pass library ID to AddBookActivity
                Intent intent = new Intent(LibraryDetailsActivity.this, AddBookActivity.class);
                intent.putExtra("library_id", libraryId);
                startActivity(intent);
            }
        });
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String libraryLocation = getIntent().getStringExtra("library_location");

                if (libraryLocation != null && !libraryLocation.isEmpty()) {
                    // Launch Google Maps Navigation
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(libraryLocation));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(LibraryDetailsActivity.this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LibraryDetailsActivity.this, "Invalid library location", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure username is passed to DashboardActivity
                Intent intent = new Intent(LibraryDetailsActivity.this, SearchActivity.class);
                intent.putExtra("username", username); // Pass the username
                startActivity(intent);
                finish();
            }
        });


    }

@Override
protected void onResume() {
    super.onResume();
    String libraryId = getIntent().getStringExtra("library_id");
    
    if (libraryId != null && !libraryId.isEmpty()) {
        // Fetch books for this library using Firebase
        firebaseHelper.getBooksByLibraryId(libraryId, new FirebaseLibraryHelper.OnBooksRetrievedListener() {
            @Override
            public void onBooksRetrieved(boolean success, List<Book> books) {
                if (success && books != null) {
                    // Use custom adapter
                    BookAdapter bookAdapter = new BookAdapter(LibraryDetailsActivity.this, books);
                    listViewBooks.setAdapter(bookAdapter);
                } else {
                    Toast.makeText(LibraryDetailsActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
//u
}

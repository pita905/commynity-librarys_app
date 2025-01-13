package com.example.finelspruject;

import android.content.Intent;
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
    Button btnBack, btnAddBook ;
    TextView txtLibraryName, txtLibraryLocation;
    ListView listViewBooks;
    LibraryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_details);

        // Initialize views
        txtLibraryName = findViewById(R.id.txtLibraryName);
        txtLibraryLocation = findViewById(R.id.txtLibraryLocation);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnBack = findViewById(R.id.btnBack);
        listViewBooks = findViewById(R.id.listViewBooks);

        dbHelper = new LibraryDatabaseHelper(this);

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
        int libraryId = getIntent().getIntExtra("library_id", -1); // Library ID to fetch books

        // Set library details
        txtLibraryName.setText("Library Name: " + libraryName);
        txtLibraryLocation.setText("Location: " + libraryLocation);

        // Fetch and display books
        List<Book> bookList = dbHelper.getBooksByLibraryId(libraryId);
        ArrayAdapter<Book> bookAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookList);
        listViewBooks.setAdapter(bookAdapter);

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int libraryId = getIntent().getIntExtra("library_id", -1); // Pass library ID to AddBookActivity
                Intent intent = new Intent(LibraryDetailsActivity.this, AddBookActivity.class);
                intent.putExtra("library_id", libraryId);
                startActivity(intent);
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
    int libraryId = getIntent().getIntExtra("library_id", -1);

    // Fetch books for this library
    List<Book> bookList = dbHelper.getBooksByLibraryId(libraryId);

    // Use custom adapter
    BookAdapter bookAdapter = new BookAdapter(this, bookList);
    listViewBooks.setAdapter(bookAdapter);
}
//u
}

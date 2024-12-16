package com.example.finelspruject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finelspruject.adapters.BookAdapter;
import com.example.finelspruject.Book;

import java.util.List;

public class LibraryDetailsActivity extends AppCompatActivity {

    TextView txtLibraryName, txtLibraryLocation;
    RecyclerView recyclerViewBooks;
    BookAdapter bookAdapter;
    LibraryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_details);

        // Initialize views
        txtLibraryName = findViewById(R.id.txtLibraryName);
        txtLibraryLocation = findViewById(R.id.txtLibraryLocation);
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);

        dbHelper = new LibraryDatabaseHelper(this);

        // Retrieve library details from intent
        String libraryName = getIntent().getStringExtra("library_name");
        String libraryLocation = getIntent().getStringExtra("library_location");
        int libraryId = getIntent().getIntExtra("library_id", -1); // Library ID to fetch books

        // Set library details
        txtLibraryName.setText("Library Name: " + libraryName);
        txtLibraryLocation.setText("Location: " + libraryLocation);

        // Fetch and display books
        List<Book> bookList = dbHelper.getBooksByLibraryId(libraryId);
        bookAdapter = new BookAdapter(bookList);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBooks.setAdapter(bookAdapter);
    }
}

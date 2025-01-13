package com.example.finelspruject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finelspruject.Book;
import com.example.finelspruject.Library;

import java.util.ArrayList;
import java.util.List;

public class LibraryDatabaseHelper extends SQLiteOpenHelper {

    // Database configuration
    private static final String DATABASE_NAME = "libraries.db";
    private static final int DATABASE_VERSION = 2;

    // Table: Libraries
    private static final String TABLE_LIBRARIES = "libraries";
    private static final String COL_LIBRARY_ID = "id";
    private static final String COL_LIBRARY_NAME = "name";
    private static final String COL_LIBRARY_LOCATION = "location";

    // Table: Books
    private static final String TABLE_BOOKS = "books";
    private static final String COL_BOOK_ID = "id";
    private static final String COL_BOOK_TITLE = "title";
    private static final String COL_BOOK_AUTHOR = "author";
    private static final String COL_BOOK_ISBN = "isbn";
    private static final String COL_BOOK_LIBRARY_ID = "library_id"; // Foreign key

    // Constructor
    public LibraryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override

    public void onCreate(SQLiteDatabase db) {
        // Create Libraries table
        db.execSQL("CREATE TABLE libraries (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "location TEXT)");

        // Create Books table
        db.execSQL("CREATE TABLE books (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    title TEXT,\n" +
                "    author TEXT,\n" +
                "    isbn TEXT,\n" +
                "    library_id INTEGER,\n" +
                "    FOREIGN KEY(library_id) REFERENCES libraries(id)\n" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS libraries");

        // Recreate the tables
        onCreate(db);
    }


    // Method to retrieve all libraries and their books
    public List<Library> getAllLibraries() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Library> libraryList = new ArrayList<>();

        // Query all libraries
        Cursor libraryCursor = db.rawQuery("SELECT * FROM " + TABLE_LIBRARIES, null);
        if (libraryCursor.moveToFirst()) {
            do {
                int libraryId = libraryCursor.getInt(0);
                String libraryName = libraryCursor.getString(1);
                String libraryLocation = libraryCursor.getString(2);

                // Get books for this library
                List<Book> bookList = new ArrayList<>();
                Cursor bookCursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS +
                        " WHERE " + COL_BOOK_LIBRARY_ID + "=?", new String[]{String.valueOf(libraryId)});
                if (bookCursor.moveToFirst()) {
                    do {
                        String title = bookCursor.getString(1);
                        String author = bookCursor.getString(2);
                        String isbn = bookCursor.getString(3);
                        bookList.add(new Book(title, author, isbn));
                    } while (bookCursor.moveToNext());
                }
                bookCursor.close();

                // Create a Library object and add it to the list
                libraryList.add(new Library(libraryName, libraryLocation, bookList));
            } while (libraryCursor.moveToNext());
        }
        libraryCursor.close();

        return libraryList;
    }
    public List<Book> getBooksByLibraryId(int libraryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Book> bookList = new ArrayList<>();

        // Query to fetch books for the given library ID
        Cursor cursor = db.rawQuery("SELECT * FROM books WHERE library_id=?", new String[]{String.valueOf(libraryId)});

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow("isbn"));

                bookList.add(new Book(title, author, isbn));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bookList;
    }
    public long addLibrary(String name, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name); // Library name
        values.put("location", location); // Library location

        // Insert into the libraries table
        long result = db.insert("libraries", null, values);
        db.close();
        return result; // Returns row ID if successful, -1 otherwise
    }
    public boolean addBookToLibraryWithImage(int libraryId, String title, String author, String imageBase64) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("library_id", libraryId);
        values.put("title", title);
        values.put("author", author);
        values.put("isbn", imageBase64); // Store the image as a Base64 string in the `isbn` column

        long result = db.insert("books", null, values);
        return result != -1; // Returns true if insertion was successful
    }

}

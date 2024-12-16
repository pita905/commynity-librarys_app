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

    // onCreate: Create tables when the database is initialized
    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // Create Libraries table
//        db.execSQL("CREATE TABLE " + TABLE_LIBRARIES + " (" +
//                COL_LIBRARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL_LIBRARY_NAME + " TEXT, " +
//                COL_LIBRARY_LOCATION + " TEXT)");
//
//        // Create Books table
//        db.execSQL("CREATE TABLE " + TABLE_BOOKS + " (" +
//                COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL_BOOK_TITLE + " TEXT, " +
//                COL_BOOK_AUTHOR + " TEXT, " +
//                COL_BOOK_ISBN + " TEXT, " +
//                COL_BOOK_LIBRARY_ID + " INTEGER, " +
//                "FOREIGN KEY(" + COL_BOOK_LIBRARY_ID + ") REFERENCES " + TABLE_LIBRARIES + "(" + COL_LIBRARY_ID + "))");
//    }
    public void onCreate(SQLiteDatabase db) {
        // Create Libraries table
        db.execSQL("CREATE TABLE libraries (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "location TEXT)");

        // Create Books table
        db.execSQL("CREATE TABLE books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "author TEXT, " +
                "isbn TEXT, " +
                "library_id INTEGER, " +
                "FOREIGN KEY(library_id) REFERENCES libraries(id))");
    }

   // @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARIES);
//        onCreate(db);
//    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS libraries");

        // Recreate the tables
        onCreate(db);
    }


    // Method to add a Library and its books
    public long addLibrary(Library library) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert library
        ContentValues libraryValues = new ContentValues();
        libraryValues.put(COL_LIBRARY_NAME, library.getName());
        libraryValues.put(COL_LIBRARY_LOCATION, library.getLocation());
        long libraryId = db.insert(TABLE_LIBRARIES, null, libraryValues);

        // Insert books associated with this library
        for (Book book : library.getBooks()) {
            ContentValues bookValues = new ContentValues();
            bookValues.put(COL_BOOK_TITLE, book.getTitle());
            bookValues.put(COL_BOOK_AUTHOR, book.getAuthor());
            bookValues.put(COL_BOOK_ISBN, book.getIsbn());
            bookValues.put(COL_BOOK_LIBRARY_ID, libraryId);
            db.insert(TABLE_BOOKS, null, bookValues);
        }

        return libraryId;
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

}
